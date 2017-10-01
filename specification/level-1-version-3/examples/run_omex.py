"""
Runs all omex examples with the sed-ml webtools.
Stores archives with results.

curl -F file=@"experiment1.omex" -o result.omex http://sysbioapps.dyndns.org/SED-ML_Web_Tools/Home/SimulatePostArchive

curl -F file=@"./__omex__/L1V3_repeated-stochastic-runs.omex" -o "./__sedml_webtools__/L1V3_repeated-stochastic-runs.omex" http://sysbioapps.dyndns.org/SED-ML_Web_Tools/Home/SimulatePostArchive

"""
from __future__ import print_function, absolute_import
import os
import warnings
import requests
import traceback

from tellurium.sedml.tesedml import executeCombineArchive
import shutil

EXAMPLES_DIR = os.path.dirname(os.path.realpath(__file__))
OMEX_DIR = os.path.join(EXAMPLES_DIR, "./__omex__")
WEBTOOL_DIR = os.path.join(EXAMPLES_DIR, "./__sedml_webtools__")
TELLURIUM_DIR = os.path.join(EXAMPLES_DIR, "./__tellurium__")
URL = "http://sysbioapps.dyndns.org/SED-ML_Web_Tools/Home/SimulatePostArchive"


def run_sedml_webtools(input_path, output_path):
    """

    :param input_path:
    :param output_path:
    :return:
    """
    print('-' * 80)
    print('Running SEDML Webtools:\n\t', input_path)
    print('-' * 80)

    files = {'file': open(input_path, 'rb')}
    response = requests.post(URL, files=files)

    # Throw an error for bad status codes
    response.raise_for_status()

    with open(output_path, 'wb') as handle:
        for block in response.iter_content(1024):
            handle.write(block)


def run_tellurium(input_path, output_path):
    """ Run Combine archive with tellurium and store archive with results.

    :param input_path:
    :param output_path:
    :return:
    """
    print('-' * 80)
    print('Running tellurium:\n\t', input_path)
    print('-' * 80)

    import tellurium
    import matplotlib
    matplotlib.use('Agg')
    tellurium.setDefaultPlottingEngine("matplotlib")

    filename, extension = os.path.splitext(os.path.basename(input_path))
    workingDir = os.path.join(TELLURIUM_DIR, '_te_{}'.format(filename))
    if not os.path.exists(workingDir):
        os.makedirs(workingDir)

    try:
        executeCombineArchive(input_path, workingDir=workingDir, outputDir=workingDir, saveOutputs=True)
        # TODO: store with results https://github.com/sys-bio/tellurium/issues/207
    except Exception:
        warnings.warn("tellurium could not run: {}".format(input_path))
        traceback.print_exc()


if __name__ == "__main__":

    for filename in os.listdir(OMEX_DIR):
        if filename.endswith('.omex') or filename.endswith('.sedx'):

            if "cellml" in filename:
                print("cellml archives not executed")
                print("\t", filename)
                continue

            input_path = os.path.join(OMEX_DIR, filename)
            output_webtools = os.path.join(WEBTOOL_DIR, filename)
            output_tellurium = os.path.join(TELLURIUM_DIR, filename)
            run_sedml_webtools(input_path, output_webtools)
            # run_tellurium(input_path, output_tellurium)
