"""
Creates the Combine Archives from the folders.
"""
from __future__ import print_function, absolute_import
import os
import warnings
import json

try:
    import libcombine
except ImportError:
    import tecombine as libcombine

import zipfile
import tempfile
import pprint



EXAMPLES_DIR = os.path.dirname(os.path.realpath(__file__))
OMEX_DIR = os.path.join(EXAMPLES_DIR, "./__omex__")

# The following archives are created
ARCHIVES = [
    'ikappab',
    'leloup-sbml',
    'lorenz-cellml',
    'lorenz-sbml',
    'oscli-nested-pulse',
    'parameter-scan-2d',
    'plotting-data-csv',
    'plotting-data-numl',
    'repeated-scan-oscli',
    'repeated-steady-scan-oscli',
    'repeated-stochastic-runs',
    'repressilator',
    'vanderpol-cellml',
    'vanderpol-sbml',
]



def create_omex(folder, omex_file, strict=True):
    """ Creates a combine archive from folder

    :param folder:
    :param omex_file:
    :param strict: strict creation, raises Errors instead of warnings
    :return:
    """
    print('*' * 100)
    print('Create OMEX:')
    print('\t', folder)
    print('*' * 100)

    if not os.path.exists(folder):
        raise IOError("Input folder does not exist:", folder)

    # delete the old omex file
    if os.path.exists(omex_file):
        os.remove(omex_file)

    json_manifest = os.path.join(folder, 'manifest.json')
    # print(json_manifest)
    with open(json_manifest, "r") as f:
        json_entries = json.load(f)
        json_entries = json_entries['entries']
        # pprint.pprint(json_entries)

    # ----------------------------------
    # Create metadata file
    # ----------------------------------
    # create the metadata file
    # <content format = "http://identifiers.org/combine.specifications/omex-metadata" location = "metadata.rdf" / >
    time_now = libcombine.OmexDescription.getCurrentDateAndTime()
    metadata_xml = []
    for entry in json_entries:
        location = entry['location']

        # add metadata for location
        description = entry.get("description", None)
        creators = entry.get("creators", None)

        if description or creators:
            # Create an omex description
            omex_d = libcombine.OmexDescription()
            omex_d.setAbout(location)
            omex_d.setCreated(time_now)

            if description:
                omex_d.setDescription(description)

            if creators:
                for c in creators:
                    creator = libcombine.VCard()
                    creator.setFamilyName(c.get("familyName", ""))
                    creator.setGivenName(c.get("givenName", ""))
                    creator.setEmail(c.get("email", ""))
                    creator.setOrganization(c.get("organisation"))
                    omex_d.addCreator(creator)

            metadata_xml.append(omex_d.toXML())

    # store the metadata file
    # FIXME: bad hack for now, but seems to work
    contents = [(item.split("\n"))[2:-2] for item in metadata_xml]
    start_str = "<?xml version='1.0' encoding='UTF-8'?>\n" \
                "<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:dcterms='http://purl.org/dc/terms/' xmlns:vCard='http://www.w3.org/2006/vcard/ns#'>"
    content_str = "\n".join(["\n".join(lines) for lines in contents])
    end_str = "</rdf:RDF>"
    metadata_str = "\n".join([start_str, content_str, end_str])

    f_metadata = os.path.join(folder, "metadata.rdf")
    with open(f_metadata, "w") as f:
        f.write(metadata_str)

    # ----------------------------------
    # Add entries to COMBINE archive
    # ----------------------------------
    archive = libcombine.CombineArchive()

    for entry in json_entries:
        location = entry['location']
        path = os.path.join(folder, location)
        if not os.path.exists(path):
            msg = "File does not exist at given location: {}".format(path)
            if strict:
                raise IOError(msg)
            else:
                warnings.warn(msg)

        # add file to archive
        format = entry['format']
        master = entry.get('master', False)
        archive.addFile(path, location, format, master)

    archive.writeToFile(omex_file)
    archive.cleanUp()
    print('Archive created:', omex_file)


if __name__ == "__main__":
    for archive_id in ARCHIVES:
        archive_dir = os.path.join(EXAMPLES_DIR, archive_id)
        omex_file = os.path.join(OMEX_DIR, "L1V3_{}.omex".format(archive_id))

        strict = True
        create_omex(archive_dir, omex_file, strict=strict)

        from tellurium.utils import omex
        omex.printArchive(omex_file)
        print('\n\n')
