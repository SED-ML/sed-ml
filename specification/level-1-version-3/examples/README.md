# Specification examples
This folder contains the specification examples and scripts to 
create the combine archives from them.

The combine archives can be created from the updated examples
by running the `create_omex.py` script. The omex files are stored in
the `__omex__` folder.

## Create Combine Archives

```
mkvirtualenv sedml-examples
(sedml-examples) pip install -r requirements.txt

```

## Specification
The specification is importing parts of the information like the sedml files 
for the SED-ML example listings. After changes to the archives make sure
that the specification `latex` document is building.
