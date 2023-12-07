# =============================================================================
# Top-level Makefile for the SED-ML specification.
# =============================================================================

MAIN   = sed-ml-L1V3

LATEX  = pdflatex
BIBTEX = bibtex

all default:
	$(LATEX) $(MAIN).tex
	$(LATEX) $(MAIN).tex
	$(BIBTEX) $(MAIN)
	$(LATEX) $(MAIN).tex
