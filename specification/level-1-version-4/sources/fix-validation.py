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
    ]


replaced_rules ={
    25504: ("valid", "The value of The value of the \element{target} attribute of an \AppliedDimension must be the identifier of an existing \RepeatedTask or \SubTask object in the document, or the identifier of a \Task referenced by a \SubTask.", "class:appliedDimension"),
    25505: ("valid", "The value of the \element{dimensionTarget} of an \AppliedDimension must be the identifier of a dimension of the referenced data.", "class:appliedDimension"),
    23505: ("valid", "The value of the \element{range} of a \RepeatedTask must be the identifier of an existing \Range child of that \RepeatedTask.", "class:range"),
    22806: ("valid", "The value of the \element{range} attribute of a \SetValue must be the identifier of an existing \Range child of the parent \RepeatedTask.", "class:setValue"),
    23105: ("valid", "The value of the \element{range} attribute of a \FunctionalRange must be the identifier of an existing \Range sibling of the \FunctionalRange.", "class:functionalRange"),
    24404: ("valid", "The value of the \element{experiment} attribute of a \ExperimentRef must be the identifier of an existing \FitExperiment child of the parent \ParameterEstimationTask.", "class:variable"),
    24608: ("valid", "The value of the \element{pointWeight} attribute of a \FitMapping must be the identifier of an existing \DataGenerator or \DataSource object in the document.", "class:fitMapping"),
    20305: ("valid", r"The attribute \token{language} on a \Model must have a value of data type \token{URN}", "class:model"),
    20306: ("valid", r"The attribute \token{source} on a \Model must have a value of data type \token{anyURI}", "class:model"),
    20706: ("valid", r"The attribute \token{target} on a \Variable must have a value of data type \token{TargetType}", "class:variable"),
    20711: ("valid", r"The attribute \token{target2} on a \Variable must have a value of data type \token{TargetType}", "class:variable"),
    20709: ("valid", r"The attribute \token{term} on a \Variable must have a value of data type \token{anyURI}", "class:variable"),
    22808: ("valid", r"The attribute \token{target} on a \SetValue must have a value of data type \token{TargetType}", "class:setValue"),
    23705: ("valid", r"The attribute \token{source} on a \DataDescription must have a value of data type \token{anyURI}", "class:dataDescription"),
    23706: ("valid", r"The attribute \token{format} on a \DataDescription must have a value of data type \token{URN}", "class:dataDescription"),
    24305: ("valid", r"The attribute \token{target} on an \AdjustableParameter must have a value of data type \token{TargetType}", "class:adjustableParameter"),
    25205: ("valid", r"The attribute \token{color} on a \Line must have a value of data type \token{SedColor}", "class:line"),
    25306: ("valid", r"The attribute \token{fill} on a \Marker must have a value of data type \token{SedColor}", "class:marker"),
    25307: ("valid", r"The attribute \token{lineColor} on a \Marker must have a value of data type \token{SedColor}", "class:marker"),
    25404: ("valid", r"The attribute \token{color} on a \Fill must have a value of data type \token{SedColor}", "class:fill"),
    }


new_rules ={
    20350: ("valid", "There must not be circular dependencies in model resolution.  The \element{source} attribute of a \Model may not directly or indirectly reference itself.", "class:model"),
    20351: ("valid", "There must not be circular cross-dependencies in model change resolution.  The \element{target} and \element{source} attributes of a \ComputeChange may not correspond to the \element{target} and \element{source} of a different \ComputeChange in a different \Model.", "class:model"),
    23150: ("valid", "There must not be circular dependencies in the calculatino of functional ranges.  No \Variable child of a \FunctionalRange may directly or indirectly reference the parent \FunctionalRange.", "class:functionalRange"),
    23550: ("valid", "There must not be circular dependencies in repeated tasks.  The \element{task} attribute of a \SubTask may not directly or indirectly reference its parent \RepeatedTask.", "class:repeatedTask"),
    23551: ("valid", "Every \RepeatedTask must have exactly one \ListOfRanges child containing at least one child \Range object.", "class:repeatedTask"),
    23552: ("valid", "Every \RepeatedTask must have exactly one \ListOfSubTasks child containing at least one child \SubTask object.", "class:repeatedTask"),
    24050: ("valid", "Every \ParameterEstimationTask must have exactly one \ListOfAdjustableParameters child containing at least one child \AdjustableParameter object.", "class:parameterEstimationTask"),
    24051: ("valid", "Every \ParameterEstimationTask must have exactly one \ListOfFitExperiments child containing at least one child \FitExperiment object.", "class:parameterEstimationTask"),
    20750: ("valid", "Every \Variable with a defined \element{dimensionTerm} attribute must have exactly one \ListOfAppliedDimensions child containing at least one child \AppliedDimension object.", "class:variable"),
    24550: ("valid", "Every \FitExperiment must have exactly one \ListOfFitMappings child containing at least one child \FitMapping object.", "class:fitExperiment"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),
    # 00000: ("valid", "", "class:"),

}

karr = """    
\item The references which are not allowed in specific contexts are not set in those contexts (e.g., no variable of a data generator has a model reference).
\item URLs should be used in place of URNs for model sources because URNs will likely be deprecated in a future version of SED-ML (warning).
\item The networks of references are acyclic:
    \begin{itemize}[labelwidth=0pt]
        \item Compute model changes.
    \end{itemize}
\item Each container element has at least one child:
    \begin{itemize}[labelwidth=0pt]
    \item Repeated tasks have at least one subtask.
    \item Reports have at least one data set.
    \item 2D plots have at least one curve.
    \item 3D plots have at least one surface.
    \end{itemize}
\item The source of each model is encoded in its language. 
    \begin{itemize}[labelwidth=0pt]
    \item CellML-encoded models: The validator uses LibCellML (\href{https://libcellml.org}{https://\allowbreak{}lib\allowbreak{}cell\allowbreak{}ml.\allowbreak{}org}) to check that each model source is a valid CellML file. 
    \item NeuroML-encoded models: The validator uses LibNeuroML \citep{vella2014libneuroml} to check that each model source is a valid NeuroML file. 
    \item SBML-encoded models: The validator uses LibSBML \citep{bornstein2008libsbml} to check that each model source is a valid SBML file. 
    \item Other XML-encoded models: The validator checks that each model source is a valid XML file. 
    \item Other formats: The validator warns users that these model sources could not be validated.
    \end{itemize}
\item For XML-encoded models, the namespaces required to describe each model change and variable of each data generator are defined.
\item For XML-encoded models, each element of each model change is an XML element or a list of XML elements. 
\item For XML-encoded models, each target of each static model change is a valid XPath.
\item Each uniform range has at least one step.
\item When a repeated task has multiple ranges, the secondary ranges are at least as long as the primary range.
\item The output start time of each time course simulation is at least its initial time.
\item The end time of each time course simulation is at least its output start time.
\item Each time course has at least one step.
\item Each time course has a number of steps evenly divisible by five (warning). This validation rule was motivated by observing that many SED-ML files in BioModels have extra unintended steps due to historical confusion about the meaning of the SED-ML attribute which stores this information and bugs in creating SED-ML files by some software tools. Note, we are helping address the underlying causes for both of these issues through revisions to the SED-ML specifications and fixes to these software tools.
\item The KiSAO id of each algorithm and the KiSAO id of each algorithm parameter are ids of KiSAO terms for specific algorithms and algorithm parameters.
\item Each parameter of an algorithm has a unique KiSAO id.
\item For XML-encoded models, each target of each data generator to a static model is a valid XPath that matches a single model element.
\item Each mathematical expression can be evaluated (e.g., all symbols are defined).
\item Each data set has a unique label within each report (warning).
\item The axes of the curves and surfaces of plots have consistent (log or linear) scales (warning).
\item The data generators for each plot are linked to basic simulation tasks and not repeated tasks (warning). The validator raises warnings for such plots because the SED-ML specifications do not officially support such plots, and some software tools do not have the capability to create these plots.
\item Each task contributes to at least one output (warning).
\item Each data generator contributes to at least one output (warning).
\item The shapes of the outputs of the sub-tasks of each repeated task are consistent (warning).
\item The shapes of the outputs of the variables of each data generator are consistent (warning).
\item The shapes of the outputs of the data sets of each report are consistent (warning).
\item The shapes of the outputs of the x and y data of each curve are consistent (warning).
\item The shapes of the outputs of the x, y, and z data of each surface are consistent (warning).    
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

def fixAndWrite(paragraph):
    global prevValidationNumber
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
