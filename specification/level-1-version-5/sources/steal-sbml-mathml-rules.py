# -*- coding: utf-8 -*-
"""
Created on Thu Aug  5 17:17:26 2021

@author: Lucian
"""

from os import walk


validdir = r"C:\Users\Lucian\Desktop\specifications\sbml-level-3\version-2\core\spec\validation-rules"
mathfile = open("apdx-validation-mathml.tex", "w")

vfiles = []
for __, _, files in walk(validdir):
    vfiles += files

for file in vfiles:
    if "102" == file[0:3]:
        nstring = file[0:5]
        mathfile.write(r"\validrule{" + nstring + r"}{")
        for line in open(validdir + r"/" + file, "r"):
            mathfile.write(line.rstrip() + " ")
        mathfile.write("}\n\n")

mathfile.close()
