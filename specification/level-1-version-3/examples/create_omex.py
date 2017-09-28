"""
Creates the Combine Archives from the folders.
"""
from __future__ import print_function, absolute_import
import os
import warnings
import json
import libcombine
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
    print(json_manifest)
    with open(json_manifest, "r") as f:
        json_entries = json.load(f)
        json_entries = json_entries['entries']
        pprint.pprint(json_entries)

    time_now = libcombine.OmexDescription.getCurrentDateAndTime()
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


        # add metadata for location
        description = entry.get("description", None)
        creators = entry.get("creators", None)


        if description or creators:
            omex_d = libcombine.OmexDescription()
            omex_d.setAbout(location)
            omex_d.setCreated(time_now)

            if description:
                omex_d.setDescription(description)
                # print("Setting description:", description)

            if creators:
                for c in creators:
                    creator = libcombine.VCard()
                    creator.setFamilyName(c.get("familyName", ""))
                    creator.setGivenName(c.get("givenName", ""))
                    creator.setEmail(c.get("email", ""))
                    creator.setOrganization(c.get("organisation"))
                    omex_d.addCreator(creator)

            archive.addMetadata(location, omex_d)


    archive.writeToFile(omex_file)
    archive.cleanUp()
    print('Archive created:', omex_file)


def printMetaDataFor(archive, location):
    """ Prints metadata for given location.

    :param archive: CombineArchive instance
    :param location:
    :return:
    """
    desc = archive.getMetadataForLocation(location)
    if desc.isEmpty():
        print("  no metadata for '{0}'".format(location))
        return None

    print("  metadata for '{0}':".format(location))
    print("     Created : {0}".format(desc.getCreated().getDateAsString()))
    for i in range(desc.getNumModified()):
        print("     Modified : {0}".format(desc.getModified(i).getDateAsString()))

    print("     # Creators: {0}".format(desc.getNumCreators()))
    for i in range(desc.getNumCreators()):
        creator = desc.getCreator(i)
        print("       {0} {1}".format(creator.getGivenName(), creator.getFamilyName()))


def printArchive(fileName):
    """ Prints content of combine archive

    :param fileName: path of archive
    :return: None
    """
    archive = libcombine.CombineArchive()
    if archive.initializeFromArchive(fileName) is None:
        print("Invalid Combine Archive")
        return None

    print('*'*80)
    print('Print archive:', fileName)
    print('*' * 80)
    printMetaDataFor(archive, ".")
    print("Num Entries: {0}".format(archive.getNumEntries()))

    for i in range(archive.getNumEntries()):
        entry = archive.getEntry(i)
        print(" {0}: location: {1} format: {2}".format(i, entry.getLocation(), entry.getFormat()))
        printMetaDataFor(archive, entry.getLocation())

        # the entry could now be extracted via
        # archive.extractEntry(entry.getLocation(), <filename or folder>)

        # or used as string
        # content = archive.extractEntryToString(entry.getLocation());

    archive.cleanUp()


if __name__ == "__main__":
    for archive_id in ARCHIVES:
        archive_dir = os.path.join(EXAMPLES_DIR, archive_id)
        omex_file = os.path.join(OMEX_DIR, "L1V3_{}.omex".format(archive_id))

        strict = True
        create_omex(archive_dir, omex_file, strict=strict)
        printArchive(omex_file)
        print('\n\n')
