#library classes to help
import math
import random
import sys

def nodeDistance(node1, node2):
	hDist = abs(node1.getX() - node2.getX())
	vDist = abs(node1.getY() - node2.getY())
	return math.sqrt(hDist**2 + vDist**2)

def dnaDistance(DNAObj1, DNAObj2):
	dna1 = DNAObj1.getDNA()
	dna2 = DNAObj2.getDNA()
	if (len(dna1) != len(dna2)): 
		return -1
	else:
		diff = 0
		for i in xrange(len(dna1)):
			if (dna1[i] != dna2[i]):
				diff+=1
		return diff


class Node:
	#both of type float
	#xcoord, ycoord
	def __init__(self, xcoord, ycoord):
		self.xcoord = xcoord
		self.ycoord = ycoord

	def getX(self):
		return self.xcoord

	def changeX(self, xcoord):
		self.xcoord = xcoord

	def getY(self):
		return self.ycoord

	def changeY(self, ycoord):
		self.ycoord = ycoord

	def distanceTo(self, node):
		return nodeDistance(self, node)


class DNA:
	#{A,C,G,T}
	#len - any length of specific length
	def __init__(self, dnaStr):
		self.dnaStr = dnaStr

	def getDNA(self):
		return self.dnaStr

	def setDNA(self, newDNAStr):
		#check if valid DNA input
		#returns -1 if error, 0 if success
		for i in xrange(len(newDNAStr)):
			if ((newDNAStr[i] != "A") or (newDNAStr[i] != "C") or (newDNAStr[i] != "G") or(newDNAStr[i] != "T")):
				self.dnaStr = None
				return False
		self.dnaStr = newDNAStr
		return True

	def validateDNA(self):
		return setDNA(self.dnaStr)

	def distanceTo(self, dna):
		return dnaDistance(self, dna)


#-----------------------------------------------------------------------------
# Random Body Generator
class BodyGenerator:

	def __init__(self, nworld_minx, nworld_maxx, nworld_miny, nworld_maxy):
		self.nworld_minx = nworld_minx
		self.nworld_maxx = nworld_maxx
		self.nworld_miny = nworld_miny
		self.nworld_maxy = nworld_maxy

	def generateRandomNodes(self, numNodes):
		bodies = []
		for i in xrange(numNodes):
			#for integer coordinates
			#x = random.randint(self.nworld_minx, self.nworld_maxx)
			#y = random.randint(self.nworld_miny, self.nworld_maxy)
			
			#for floating point coordinates
			x = random.uniform(self.nworld_minx, self.nworld_maxx)
			y = random.uniform(self.nworld_miny, self.nworld_maxy)
			bodies.append(Node(x,y))
		return bodies

	def printNodes(self, nodeList):
		# nodeList = self.generateRandomNodes(numNodes)
		resList = []
		for node in nodeList:
			resList.append((node.getX(), node.getY()))
		print resList

	def generateRandomDNA(self, lengthDNA, numNodes):
		#{A,C,G,T}
		bases = ["A", "C", "G", "T"]
		dnaNodes = []
		for i in xrange(numNodes):
			dna = ""
			for j in xrange(lengthDNA):
				dna += random.choice(bases)
			dnaNodes.append(DNA(dna))
		return dnaNodes

	def printDNANodes(self, DNAlist):
		# DNAlist = self.generateRandomDNA(lengthDNA, numNodes)
		resList = []
		for dna in DNAlist:
			resList.append(dna.getDNA())
		print resList


