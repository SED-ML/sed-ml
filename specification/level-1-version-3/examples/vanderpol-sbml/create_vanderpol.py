"""
Create the SBML model.
"""
import antimony

model_id = 'vanderpol'
# ----------------------------
model_str = '''
model {}

var species x = -2; 
var species y = 0;
const mu = 1;

J1: -> x; y
J2: -> y; mu *(1-x^2)*y - x

end
'''.format(model_id)
# ----------------------------

antimony.setBareNumbersAreDimensionless(True)
antimony.loadAntimonyString(model_str)
model_file = './vanderpol-sbml.xml'
antimony.writeSBMLFile(model_file, model_id)
print(model_file)