import random
import math
import sys
from mpi4py import MPI
import time


initTime = time.time()
#-------------------------------------------------------
(minx, miny, maxx, maxy) = (0, 0, 100, 100)
lengthDNA = 20
numCentroids = 8
numNodes = 1000
modes = ["node", "DNA"]
mode = modes[0]
#-------------------------------------------------------
assert (numNodes >= numCentroids)


def nodeDistance(src, dest):
	assert(type(src) == type(dest))
	if (type(src) is str):
		#DNA
		assert(len(src) == len(dest))
		diff = 0
		for i in xrange(len(src)):
			if (src[i] != dest[i]):
				diff+=1
		return diff
	else:
		#(2d nodes as tuple)
		hDist = abs(src[0] - dest[0])
		vDist = abs(src[1] - dest[1])
		return math.sqrt(hDist**2 + vDist**2)

#partitions an array into n parts
def chunks(lst, n):
    division = len(lst) / float(n)
    #create a list of partiitons
    lstChunks = [lst[int(round(division * i)):int(round(division * (i + 1)))] for i in xrange(n)]
    assert (len(lstChunks) == n)
    return lstChunks

#partitions a dictionary into numProcesses parts
def chunkDictionary(d, numProcesses):
	l = d.keys()
	l2dkeys = chunks(l, numProcesses)
	newListDict = []
	for lstKeys in l2dkeys:
		newD = {}
		for k in lstKeys:
			newD[k] = d.get(k)
		newListDict.append(newD)
	assert (len(newListDict) == numProcesses)
	return newListDict

def generateRandomDNA(numNodes, lengthDNA):
	#{A,C,G,T}
	bases = ["A", "C", "G", "T"]
	dnaNodes = []
	for i in xrange(numNodes):
		dna = ""
		for j in xrange(lengthDNA):
			dna += random.choice(bases)
		dnaNodes.append(dna)
	return dnaNodes

def generateInputs(numNodes, inputType=None):
	if (inputType == "DNA"):
		return generateRandomDNA(numNodes, lengthDNA)
	else:
		nodes = []
		for i in xrange(numNodes):
			x = random.uniform(minx, maxx)
			y = random.uniform(miny, maxy)
			nodes.append((x,y))
		return nodes

def getDNACentroid(nodes, lengthDNA):
	newC = ""
	for i in xrange(lengthDNA):
		aCnt = 0
		cCnt = 0
		gCnt = 0
		tCnt = 0
		for j in nodes:
			base = j[i]
			if (base == "A"):
				aCnt += 1
			elif (base == "C"):
				cCnt += 1
			elif (base == "G"):
				gCnt += 1
			elif (base == "T"):
				tCnt +=1
			else:
				print "ERROR: Shouldn't have reached here. In DNA recluster"

		#now find the maximum and substitute it for that character.
		cnts = {"A" : aCnt, "C" : cCnt, "G" : gCnt, "T" : tCnt}
		currCount = 0
		modeBase = "A"
		for k,v in cnts.items():
			if (v>currCount):
				modeBase = k
		newC+=modeBase
	return newC

def getNewCentroid(nodes, inputType=None):
	if (inputType == "DNA"):
		return getDNACentroid(nodes, lengthDNA)
	else:
		x = 0
		y = 0
		n = float(len(nodes))
		for v in nodes:
			x+=v[0]
			y+=v[1]
		x /= n
		y /= n
		newCentroid = (x,y)
		return newCentroid


#-------------------------------------------------------
nodes = generateInputs(numNodes, mode)
lstOfCentroids = random.sample(nodes, numCentroids)
cents = dict((cent,[]) for cent in lstOfCentroids)

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

assert(numNodes >= size)
assert(numCentroids >= size)

numIterations = 5
iteratorCounter = -1
while (iteratorCounter < numIterations):
	iteratorCounter += 1

	#-------------------------
	#SET INIT BODIES:
	#Initialize the hashmap with centroids mapped to points

	if (rank == 0):
		centroidHash = cents
		allNodes = chunks(nodes, size)
	else:
		centroidHash = None
		allNodes = None

	# broadcast centroidHash
	# init centroidDictionary = centroidHash
	# random_select_cent1 -> []
	# random_select_cent2 -> []
	# random_select_centk -> []

	#scatterAllNodes
	# allNodes = 2d array of all nodes total
	# [[n1, n2], 
	#  [n3, n4, n5], 
	#  [n6,n7]]

	centroidHash = comm.bcast(centroidHash, root=0)
	nodesToCenter = comm.scatter(allNodes, root=0)

	# nodesToCenter = [n1,n2]
	# nodesToCenter = [n3,n4,n5]
	# nodesToCenter = [n6,n7]

	#go through each lst of nodes that the process has
	#add it to the hash map of centroids
	for i in nodesToCenter:
		dist = sys.maxint
		closest = None
		for j in centroidHash.keys():
			x = nodeDistance(i,j)
			if (x<dist):
				dist = x
				closest = j
		toAddTo = centroidHash.get(closest)
		if (toAddTo != None):
			toAddTo.append(i)
		else:
			print "ERROR: Shouldn't have reached here"

	# gather all the centroidHash into a list of centroidHashes
	# centroidHash = [ {n1 : [n2,n3]}, {n4 : [n7, n6]}, {n5 : n5}]
	centroidHash = comm.gather(centroidHash, root=0)

	if rank == 0:
		#totalCentroidHash : collects all results from the different processes
		totalCentroidHash = {}
		for c in centroidHash:
			for k in c.keys():
				v = c.get(k)
				if k in totalCentroidHash:
					totalCentroidHash[k] += v
				else:
					totalCentroidHash[k] = v
	else:
		totalCentroidHash = None
		assert centroidHash is None


	if (iteratorCounter == numIterations):
		# print "TERMINATING"
		break

	#--------------------------------------------------
	#RECLUSTERING:
	#begins by partitioning an existing (supporting) dictionary 
	#to get ready for recalculation of centroids
	#Take an existing hashmap named: totalCentroidHash
	#scatter the different dictionary parts across the processes

	if rank == 0:
		totalCentroidHashParts = chunkDictionary(totalCentroidHash, size)
	else:
		totalCentroidHashParts = None
	partCentroidHash = comm.scatter(totalCentroidHashParts, root=0)

	#now each process has a full hash map, of a partition of the centroids
	#now calculate the new centroids by going through each centroid
	#and calculating a new (node)
	newPartCentroidHash = {}

	for value in partCentroidHash.values():
		newCentroid = getNewCentroid(value, mode)
		newPartCentroidHash[newCentroid] = []


	# bring back all the partitions of centroids and bring them together
	newEmptyTotalCentroidHash = comm.gather(newPartCentroidHash, root=0)

	#ideally none of the keys should conflict by this point
	if (rank == 0):
		#collect all the hashmaps into one and then it should be in the same state as cents at the top
		l = []
		for h in newEmptyTotalCentroidHash:
			l += h.items()
		newEmptyTotalCentroidHash = dict(l)

	else:
		assert (newEmptyTotalCentroidHash == None)

	cents = newEmptyTotalCentroidHash

if (totalCentroidHash):
	print "ENDED"
	print (time.time() - initTime)
