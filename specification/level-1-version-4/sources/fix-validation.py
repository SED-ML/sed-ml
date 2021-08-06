# -*- coding: utf-8 -*-
"""
Created on Thu Jul  8 14:00:04 2021

@author: Lucian
"""

import re
import math

infile = open("apdx-validation-orig.tex", "r")
outfile = open("apdx-validation.tex", "w")

replacements = [
    ("SBML", "SED-ML"),
    ("an SED", "a SED"),
    ("an \SED", "a \\SED"),
    (r"Rule{sedml-", r"Rule{"),
    ("http://www.sbml.org/sbml/level1/version1/sedml/version1", "http://sed-ml.org/sed-ml/level1/version4"),
    ("\label", "\n\\label"),
    ("an SED-ML Level~1 Version~1 model that uses the \SEDMLPackage. We use the same conventions as are used in the SED-ML Level~1 Version~1 Core specification document. In particular, there", "SED-ML.  There"),
    ("\SEDMLPackage", "SED-ML"),
    ("the SED-ML package", "SED-ML"),
    ("SED-ML Level~1 Specification for SEDML, Version~1", "SED-ML Level~1 Version~4"),
    ("SED-ML Level~1 Version~1", "SED-ML Level~1 Version~4"),
    ("SED-ML Level~3 Core", "SED-ML Level~1"),
    ("\sbmlthreecore", "\\currentLV"),
    ("SED-ML Level~3 Version~1 Core", "SED-ML Level~1 Version~4"),
    ("\sec{", "Section \\ref{"),
    ("\primtype", "\\code"),
    ("\class{SED-ML}", "\\SedML"),
    ("\TODO", "%\\TODO"),
    ("\Document", "\\SedDocument"),
    ("\PlotTwoD", "\\PlotTwo"),
    ("\PlotThreeD", "\\PlotThree"),
    ("\ListOfAbstractCurves", "\\ListOfCurves"),
    ("\ListOfAbstractTasks", "\\ListOfTasks"),
    ("\ListOfSetValues", "\\ListOfChanges"),
    ("primitive-types", "sec:dataTypes"),
    ("xml-namespace", "sec:xmlns"),
    ("abstractcurve-class", "curve-class"),
    ("abstracttask-class", "task-class"),
    ("abstractcurves-class", "curves-class"),
    ("abstracttasks-class", "tasks-class"),
    ("plottwod-class", "plot2D-class"),
    ("plotthreed-class", "plot3D-class"),
    ("document-class", "sed-ml-class"),
    ("applieddimension", "appliedDimension"),
    ("algorithmparameter", "algorithmParameter"),
    ("uniformtimecourse", "uniformTimeCourse"),
    ("remainingdimension", "remainingDimension"),
    ("changeattribute", "changeAttribute"),
    ("datadescription", "dataDescription"),
    ("changexml", "changeXml"),
    ("datagenerator", "dataGenerator"),
    ("addxml", "addXml"),
    ("onestep", "oneStep"),
    ("simplerepeatedtask", "simpleRepeatedTask"),
    ("repeatedtask", "repeatedTask"),
    ("subtask", "subTask"),
    ("functionalrange", "functionalRange"),
    ("uniformrange", "uniformRange"),
    ("dataset", "dataSet"),
    ("vectorrange", "vectorRange"),
    ("computechange", "computeChange"),
    ("datasource", "dataSource"),
    ("listofsetvalues-class", "sec:changesModel"),
    ("listofchanges-class", "sec:changesModel"),
    ("setvalue", "setValue"),
    ("shadedarea", "shadedArea"),
    ("dependentvariable", "dependentVariable"),
    ("subplot", "subPlot"),
    ("fitmapping", "fitMapping"),
    ("fitexperiment", "fitExperiment"),
    ("experimentref", "experimentRef"),
    ("adjustableparameter", "adjustableParameter"),
    ("parameterestimationtask", "parameterEstimationTask"),
    ("shadedarea", "shadedArea"),
    ("datarange", "dataRange"),
    ("parameterestimationresultplot", "parameterEstimationResultPlot"),
    ("waterfallplot", "waterfallPlot"),
    ("parameterestimationreport", "parameterEstimationReport"),
    ("(Extends validation rule \#10301 in the \currentLV specification. TO DO list scope of ids)", "The value of the attribute id on every SED-ML object must be unique across the set of all id attribute values of all objects in a SED-ML document."),
    ("(Reference: SED-ML Level~1 Version~4, Section~3.1.7.)", "(Reference: SED-ML Level~1 Version~4, Section~\\ref{sec:id}.)"),
    (r"SedDocument object may have the optional SED-ML Level~1 attributes \token{metaid}", "SedDocument object may have the optional SED-ML Level~1 attributes \\token{id}, \\token{metaid},"),
    (r"\token{sedml:\-", "\\token{"),
    ("SED-ML Level~3 SEDML namespaces", "SED-ML namespace"),
    ("Reference: SED-ML Level~1 Version~4, Section~3.2", "Reference: SED-ML Level~1 Version~4, Section~\\ref{class:sedBase}"),
    (r"may have the optional SED-ML Level~1 attributes \token{metaid} and \token{sboTerm}.", r"may have the optional SED-ML Level~1 attributes \token{id}, \token{name}, and \token{metaid}."),
    (r"optional SED-ML Level~1 attributes \token{id}, \token{metaid}, and \token{sboTerm}", r"optional SED-ML Level~1 attributes \token{id}, \token{name}, and \token{metaid}"),
    (r"the enclosing \Model object", "the document"),
    ("rangeId", "range"),
    ("symbolTwo", "symbol2"),
    ("targetTwo", "target2"),
    ("\\notice ", ""),
    ("modelReference", "model\-Reference"),
    ("outputStartTime", "output\-Start\-Time"),
    ("numberOfSteps", "number\-Of\-Steps"),
    ("experimentalCondition", "experimental\-Condition"),
    ("yDataReference", "yData\-Reference"),
    ('3.1.6', '\\ref{sec:metaid}'),
    (r"value of data type \token{integer}, and must be non negative", r"positive value of data type \token{integer}"),
    (r"{Plot2D}", r"{PlotTwo}"),
    (r"{Plot3D}", r"{PlotThree}"),
    ]


replaced_rules ={
    20305: ("valid", r"The attribute \token{language} on a \Model must have a value of data type \token{URN}", "class:model"),
    20306: ("valid", r"The attribute \token{source} on a \Model must have a value of data type \token{anyURI}", "class:model"),
    20706: ("valid", r"The attribute \token{target} on a \Variable must have a value of data type \token{TargetType}", "class:variable"),
    20709: ("valid", r"The attribute \token{term} on a \Variable must have a value of data type \token{anyURI}", "class:variable"),
    20711: ("valid", r"The attribute \token{target2} on a \Variable must have a value of data type \token{TargetType}", "class:variable"),
    22806: ("valid", "The value of the \element{range} attribute of a \SetValue must be the identifier of an existing \Range child of the parent \RepeatedTask.", "class:setValue"),
    22808: ("valid", r"The attribute \token{target} on a \SetValue must have a value of data type \token{TargetType}", "class:setValue"),
    23105: ("valid", "The value of the \element{range} attribute of a \FunctionalRange must be the identifier of an existing \Range sibling of the \FunctionalRange.", "class:functionalRange"),
    23505: ("valid", "The value of the \element{range} of a \RepeatedTask must be the identifier of an existing \Range child of that \RepeatedTask.", "class:range"),
    23705: ("valid", r"The attribute \token{source} on a \DataDescription must have a value of data type \token{anyURI}", "class:dataDescription"),
    23706: ("valid", r"The attribute \token{format} on a \DataDescription must have a value of data type \token{URN}", "class:dataDescription"),
    24305: ("valid", r"The attribute \token{target} on an \AdjustableParameter must have a value of data type \token{TargetType}", "class:adjustableParameter"),
    24404: ("valid", "The value of the \element{experiment} attribute of a \ExperimentReference must be the identifier of an existing \FitExperiment child of the parent \ParameterEstimationTask.", "class:variable"),
    24608: ("valid", "The value of the \element{pointWeight} attribute of a \FitMapping must be the identifier of an existing \DataGenerator or \DataSource object in the document.", "class:fitMapping"),
    25205: ("valid", r"The attribute \token{color} on a \Line must have a value of data type \token{SedColor}", "class:line"),
    25306: ("valid", r"The attribute \token{fill} on a \Marker must have a value of data type \token{SedColor}", "class:marker"),
    25307: ("valid", r"The attribute \token{lineColor} on a \Marker must have a value of data type \token{SedColor}", "class:marker"),
    25404: ("valid", r"The attribute \token{color} on a \Fill must have a value of data type \token{SedColor}", "class:fill"),
    25504: ("valid", "The value of The value of the \element{target} attribute of an \AppliedDimension must be the identifier of an existing \RepeatedTask or \SubTask object in the document, or the identifier of a \Task referenced by a \SubTask.", "class:appliedDimension"),
    25505: ("valid", "The value of the \element{dimensionTarget} of an \AppliedDimension must be the identifier of a dimension of the referenced data.", "class:appliedDimension"),
    }


new_rules ={
    20250: ("modeling", r"Every \SedDocument should contain at least one \Output.", "class:sed-ml"),
    20350: ("valid", "There must not be circular dependencies in model resolution.  The \element{source} attribute of a \Model may not directly or indirectly reference itself.", "class:model"),
    20351: ("valid", "There must not be circular cross-dependencies in model change resolution.  The \element{target} and \element{source} attributes of a \ComputeChange may not correspond to the \element{target} and \element{source} of a different \ComputeChange in a different \Model.", "class:model"),
    20352: ("valid", "The model pointed to by the \element{source} attribute must exist.", "class:model"),
    20353: ("valid", "The model pointed to by the \element{source} attribute must be encoded in the language defined by the \element{language} attribute.", "class:model"),
    20354: ("modeling", "Avoid using URNs for the \element{source} attribute of a \Model, as these have become increasingly harder to resolve.", "class:model"),
    20355: ("valid", r"A \Model may only contain an \AddXML child if its \element{language} attribute describes an XML-based language.", "class:model"),
    20356: ("valid", r"A \Model may only contain a \RemoveXML child if its \element{language} attribute describes an XML-based language.", "class:model"),
    20357: ("valid", r"A \Model may only contain a \ChangeXML child if its \element{language} attribute describes an XML-based language.", "class:model"),
    20550: ("valid", "The \element{target} attribute of an \AddXML object must point to a valid target in the \Model \element{source}.", "class:change"),
    20551: ("valid", "The \element{target} attribute of an \AddXML object be a valid XPath when the \Model \element{language} attribute points to an XML-based language.", "class:change"),
    20552: ("valid", r"The XML child of an \AddXML object must be a valid XML element or list of XML elements.", "class:addXml"),
    20553: ("valid", r"The XML child of an \AddXML object must be in a namespace defined by the \element{source} attribute of the parent \Model object, or explicitly define its own namespace understood by the language of the target model.", "class:addXml"),
    20650: ("valid", "The \element{target} attribute of a \ChangeAttribute object must point to a valid target in the \Model \element{source}.", "class:change"),
    20651: ("valid", "The \element{target} attribute of a \ChangeAttribute object be a valid XPath when the \Model \element{language} attribute points to an XML-based language.", "class:change"),
    20750: ("valid", "Every \Variable with a defined \element{dimensionTerm} attribute must have exactly one \ListOfAppliedDimensions child containing at least one child \AppliedDimension object.", "class:variable"),
    20751: ("valid", "Every \Variable with a defined \element{target2} or \element{symbol2} attribute must also define the \element{term} attribute.", "class:variable"),
    20752: ("valid", "The \element{target}, \element{symbol}, \element{term}, \element{target2} and \element{symbol2} attributes of a \Variable must collectively define a single mathematical concept.", "class:variable"),
    20753: ("valid", r"When the \element{target} attribute of a \Variable is an XPath, it must point to a single model element or attribute.", "class:variable"),
    21050: ("consistency", r'Avoid use of the \element{numberOfPoints} attribute of a \UniformTimeCourse in favor of the \element{numberOfSteps} attribute.  "Number of Steps" accurately reflects the meaning of the attribute.', "class:uniformTimeCourse"),
    21051: ("valid", r"The value of the \element{outputStartTime} attribute of a \UniformTimeCourse must be equal to or greater than the value of the \element{initialTime} attribute.", "class:uniformTimeCourse"),
    21052: ("valid", r"The value of the \element{endTime} attribute of a \UniformTimeCourse must be equal to or greater than the value of the \element{outputStartTime} attribute.", "class:uniformTimeCourse"),
    21053: ("modeling", r"The value of the \element{numberOfPoints} attribute of a \UniformTimeCourse should typically be evenly divisible by five.  When this is not the case, it often indicates that the modeler is unaware that the definition of the attribute is actually 'the number of points not including the initial state'.", "class:uniformTimeCourse"),
    21150: ("valid", r"The value of the \element{kisaoID} attribute of an \Algorithm must be the ID of an algorithm in the KiSAO ontology.", "class:algorithm"),
    21250: ("modeling", r"Every \AbstractTask should contribute to at least one \Output.", "class:abstractTask"),
    21550: ("modeling", r"Every \DataGenerator should contribute to at least one \Output.", "class:dataGenerator"),
    21551: ("modeling", r"The shape of the output of every \Variable child of the same \DataGenerator should either be scalar or be consistent with its \Variable siblings.", "class:dataGenerator"),
    21750: ("modeling", r"The shape of the data referenced by every \AbstractCurve child of a single \PlotTwo object should be consistent", "class:plot2D"),
    21850: ("modeling", r"The shape of the data referenced by every \Surface child of a single \PlotThree object should be consistent", "class:plot3D"),
    21950: ("consistency", r"No \element{logX} attribute of any \AbstractCurve should be set.  Instead, the \element{type} attribute of the corresponding \Axis should be used.", "class:abstractCurve"),
    22050: ("consistency", r"No \element{logY} attribute of any \Curve should be set.  Instead, the \element{type} attribute of the corresponding \Axis should be used.", "class:curve"),
    22150: ("consistency", r"No \element{logX}, \element{logY}, or \element{logZ} attribute of any \Surface should be set.  Instead, the \element{type} attribute of the corresponding \Axis should be used.", "class:abstractCurve"),
    22250: ("modeling", r"The \element{label} attributes of the \DataSet children of a single \Report should be unique for clarity.", "class:dataSet"),
    22350: ("modeling", r"The shape of the output of every \DataSet child of the same \Report should be consistent.", "class:report"),
    22450: ("valid", r"The value of the \element{kisaoID} attribute of an \AlgorithmParameter must be the ID of an algorithm parameter in the KiSAO ontology that is associated with the \element{kisaoID} of its parent \Algorithm.", "class:algorithmParameter"),
    22451: ("valid", r"The value of the every \element{kisaoID} attribute of the \AlgorithmParameter children of a single \Algorithm must be unique.", "class:algorithmParameter"),
    22650: ("valid", "The \element{target} attribute of a \ChangeXML object must point to a valid target in the \Model \element{source}.", "class:change"),
    22651: ("valid", "The \element{target} attribute of a \ChangeXML object be a valid XPath when the \Model \element{language} attribute points to an XML-based language.", "class:change"),
    22652: ("valid", r"The XML child of an \ChangeXML object must be a valid XML element or list of XML elements.", "class:changeXml"),
    22653: ("valid", r"The XML child of an \ChangeXML object must be in a namespace defined by the \element{source} attribute of the parent \Model object, or explicitly define its own namespace understood by the language of the target model.", "class:changeXml"),
    22750: ("valid", "The \element{target} attribute of a \RemoveXML object must point to a valid target in the \Model \element{source}.", "class:change"),
    22751: ("valid", "The \element{target} attribute of a \RemoveXML object be a valid XPath when the \Model \element{language} attribute points to an XML-based language.", "class:change"),
    22752: ("valid", r"The XML child of an \RemoveXML object must be a valid XML element or list of XML elements.", "class:removeXml"),
    22753: ("valid", r"The XML child of an \RemoveXML object must be in a namespace defined by the \element{source} attribute of the parent \Model object, or explicitly define its own namespace understood by the language of the target model.", "class:removeXml"),
    22950: ("consistency", r'Avoid use of the \element{numberOfPoints} attribute of a \UniformRange in favor of the \element{numberOfSteps} attribute.  "Number of Steps" accurately reflects the meaning of the attribute.', "class:uniformRange"),
    22951: ("modeling", r"The value of the \element{numberOfPoints} attribute of a \UniformRange should typically be evenly divisible by five.  When this is not the case, it often indicates that the modeler is unaware that the definition of the attribute is actually `the number of points not including the initial state'.", "class:uniformRange"),
    23150: ("valid", "There must not be circular dependencies in the calculatino of functional ranges.  No \Variable child of a \FunctionalRange may directly or indirectly reference the parent \FunctionalRange.", "class:functionalRange"),
    23550: ("valid", "There must not be circular dependencies in repeated tasks.  The \element{task} attribute of a \SubTask may not directly or indirectly reference its parent \RepeatedTask.", "class:repeatedTask"),
    23551: ("valid", "Every \RepeatedTask must have exactly one \ListOfRanges child containing at least one child \Range object.", "class:repeatedTask"),
    23552: ("valid", "Every \RepeatedTask must have exactly one \ListOfSubTasks child containing at least one child \SubTask object.", "class:repeatedTask"),
    23553: ("valid", "When a \RepeatedTask has multiple \Range children, they all must have at least as many entries as the one referenced by the \element{range} attribute.", "class:repeatedTask"),
    23554: ("modeling", r"The shape of the output of every \SubTask child of the same \RepeatedTask should be consistent", "class:repeatedTask"),
    23650: ("valid", "The \element{target} attribute of a \ComputeChange object must be a valid XPath when the \Model \element{language} attribute points to an XML-based language.", "class:change"),
    23651: ("valid", "The \element{target} attribute of a \ComputeChange object be a valid XPath when the \Model \element{language} attribute points to an XML-based language.", "class:change"),
    24050: ("valid", "Every \ParameterEstimationTask must have exactly one \ListOfAdjustableParameters child containing at least one child \AdjustableParameter object.", "class:parameterEstimationTask"),
    24051: ("valid", "Every \ParameterEstimationTask must have exactly one \ListOfFitExperiments child containing at least one child \FitExperiment object.", "class:parameterEstimationTask"),
    24550: ("valid", "Every \FitExperiment must have exactly one \ListOfFitMappings child containing at least one child \FitMapping object.", "class:fitExperiment"),
    # 00000: ("valid", r"", "class:"),
}

karr = """
\item The references which are not allowed in specific contexts are not set in those contexts (e.g., no variable of a data generator has a model reference).
\item Each mathematical expression can be evaluated (e.g., all symbols are defined).
"""

def addRequiredId(paragraph):
    idreqlist = ["\\Parameter", "\\Variable", "\\DataDescription", "\\DataSource", "\\Model", "\\Simulation", "\\AbstractTask", "\\Task", "\\RepeatedTask", "\\ParameterEstimationTask", "\\DataGenerator", "\\Style", ]
    for req in idreqlist:
        if req + " object may have the optional SED-ML Level~1 attributes" in paragraph:
            return paragraph.replace(r"optional SED-ML Level~1 attributes \token{id}, \token{name},", r"required SED-ML Level~1 attribute \token{id} and the optional attributes \token{name}")
    return paragraph

def listOfUpper(letter):
    return "listOf" + letter.group(1).upper()

res = [
    (r"\\class{(.*?)}", r"\\\1"),
    (r"\\ref{(.*?)-class}", r"\\ref{class:\1}"),
    (r"listof(.)", listOfUpper),
    (r"No other elements from the SED-ML .*?namespaces are permitted on a.*?\. ", ""),
    (r"No other elements from the SED-ML .*?namespace are permitted on a.*?\. ", ""),
    (r"No other element from the SED-ML namespaces is permitted on a.*?\. ", ""),
    (r"No other attributes from the SED-ML.*namespaces are permitted on a.*?\. ", ""),
    (r"No other attributes from the SED-ML namespace are permitted on a.*?\. ", ""),
    (r"Rules for(.*?) object}$", r"Rules for\1 objects}"),
    (r"\\SedDocument objects", r"the \\SedDocument object"),
    ]

removed_rules = [
    20101,
    20102,
    20103,
    25405,
    ]

ignore = [
    "Rules for the extended \SedML class",
    ]

def fix(paragraph):
    paragraph = paragraph.strip()
    for replacement in replacements:
        paragraph = paragraph.replace(replacement[0], replacement[1])
    for regexp in res:
        paragraph = re.sub(regexp[0], regexp[1], paragraph)
    for ig in ignore:
        if ig in paragraph:
            return ""
    paragraph = addRequiredId(paragraph)
    # paragraph = re.sub("")
    return paragraph;

def writeNewRule(rnum):
    valid, text, ref = new_rules[rnum]
    outfile.write("\\" + valid + "Rule{" + str(rnum) + "}{" + text + "  (Reference: SED-ML Level~1 Version~4, Section \\ref{" + ref + "})}\n\n")

def writeReplacedRule(rnum):
    valid, text, ref = replaced_rules[rnum]
    outfile.write("\\" + valid + "Rule{" + str(rnum) + "}{" + text + "  (Reference: SED-ML Level~1 Version~4, Section \\ref{" + ref + "})}\n\n")

def writeNewBefore(val):
    deletelist = []
    for rnum in new_rules:
        if rnum < val:
            writeNewRule(rnum)
            deletelist.append(rnum)
    for deleteme in deletelist:
        del new_rules[deleteme]

prevValidationNumber = 0

def getValidationNumber(paragraph):
    pvec = re.split("{|}", paragraph)
    print(pvec)
    if len(pvec) > 1:
        if pvec[1].isnumeric():
            print(pvec[1])
            return int(pvec[1])
        else:
            print("0")
            return 0
    print(str(prevValidationNumber))
    return prevValidationNumber

writeMath = True

def fixAndWrite(paragraph):
    global prevValidationNumber, writeMath
    paragraph = fix(paragraph)
    num = getValidationNumber(paragraph)
    if num==0:
        target = math.floor(prevValidationNumber/100)*100+100
        print("valid: ", str(target))
        writeNewBefore(target)
    else:
        writeNewBefore(num)
    prevValidationNumber = num
    if num in replaced_rules:
        writeReplacedRule(num)
    elif num not in removed_rules:
        outfile.write(paragraph + "\n\n")
    elif writeMath:
        outfile.write("\\input{sources/apdx-validation-mathml.tex}\n\n")
        writeMath = False

paragraph = ""
for line in infile:
    line = line.rstrip()
    if line == "" and paragraph != "":
        fixAndWrite(paragraph)
        paragraph = ""
    else:
        paragraph += " " + line

fixAndWrite(paragraph)
writeNewBefore(100000)

infile.close()
outfile.close()
