"""
Runs all omex examples with the sed-ml webtools to
create the results.

curl -F file=@"experiment1.omex" -o result.omex http://sysbioapps.dyndns.org/SED-ML_Web_Tools/Home/SimulatePostArchive

curl -F file=@"./__omex__/L1V3_repeated-stochastic-runs.omex" -o "./__sedml_webtools__/L1V3_repeated-stochastic-runs.omex" http://sysbioapps.dyndns.org/SED-ML_Web_Tools/Home/SimulatePostArchive

"""
from __future__ import print_function, absolute_import
import os
import requests

EXAMPLES_DIR = os.path.dirname(os.path.realpath(__file__))
OMEX_DIR = os.path.join(EXAMPLES_DIR, "./__omex__")
WEBTOOL_DIR = os.path.join(EXAMPLES_DIR, "./__sedml_webtools__")
URL = "http://sysbioapps.dyndns.org/SED-ML_Web_Tools/Home/SimulatePostArchive"

for filename in os.listdir(OMEX_DIR):
    if filename.endswith('.omex') or filename.endswith('.sedx'):

        input_path = os.path.join(OMEX_DIR, filename)
        output_path = os.path.join(WEBTOOL_DIR, filename)
        print('-' * 80)
        print('Running SEDML Webtools:\n\t', output_path)
        print('-' * 80)

        if "cellml" in  filename:
            print("cellml archives not executed with SEDML webtools")
            print("\t", filename)
            continue

        files = {'file': open(input_path, 'rb')}
        response = requests.post(URL, files=files)

        # Throw an error for bad status codes
        response.raise_for_status()

        with open(output_path, 'wb') as handle:
            for block in response.iter_content(1024):
                handle.write(block)
