# -*- coding: utf-8 -*-
"""
Created on Thu Jul  8 14:00:04 2021

@author: Lucian
"""

import re

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
    ]

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
    (r"No other attributes from the SED-ML namespace are permitted on a.*?\. ", "")
    ]

ignore = [
    "sedml-2010",
    "Rules for the extended \SedML class"
    ]

def fix(paragraph):
    paragraph = paragraph.strip()
    for replacement in replacements:
        paragraph = paragraph.replace(replacement[0], replacement[1])
    for regexp in res:
        paragraph = re.sub(regexp[0], regexp[1], paragraph)
    for ig in ignore:
        if ig in paragraph:
            return
    paragraph = addRequiredId(paragraph)
    # paragraph = re.sub("")
    outfile.write(paragraph + "\n\n")

paragraph = ""
for line in infile:
    line = line.rstrip()
    if line == "":
        fix(paragraph)
        paragraph = ""
    else:
        paragraph += " " + line

fix(paragraph)

infile.close()
outfile.close()
