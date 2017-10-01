# SED-ML Specification examples
This folder contains the specification examples and scripts to create the respective COMBINE archives
and execute the archives.

## Specification
The latex specification document is importing parts of example information, i.e.,
- SED-ML files for the example listings
- results figures

After updating the archives changes to the archives make sure that the specification `latex` document is building.

## Create Combine Archives
All combine archives can be created from the example folders
by running the `create_omex.py` script. The output omex files are stored in
the `__omex__` folder.

```
mkvirtualenv sedml-examples
(sedml-examples) pip install -r requirements.txt
python create_omex.py
```

## Run archives
The COMBINE archives can be executed using the `run_omex.py` script.
The archives with results are created in `__tellurium__` and `__sedml_webtools__`, respectivly.
Archives containing CellML models are not executed via the script. Such archives have to 
be executed with SEDML web tools and OpenCOR manually.


