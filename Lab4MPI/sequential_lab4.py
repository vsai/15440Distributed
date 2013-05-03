from lib import *

#Abstract World Class
class World:
	#lstOfBodies = array of Nodes
	def __init__(self, lstOfBodies, numCentroids):
		self.lstOfBodies = lstOfBodies
		self.numCentroids = numCentroids
		self.centroids = {}
		self.setInitCentroids()
		self.setBodies()

	def setInitCentroids(self):
		#get a random sample of k bodies as your centroids
		#initialize the dictionary with keys = centroids, and
		#values representing the bodies associated with them
		lstOfCentroids = random.sample(self.lstOfBodies, self.numCentroids)
		for i in lstOfCentroids:
			self.centroids[i] = []
		#select self.numCentroids out of the lstOfBodies

	def setBodies(self):
		for i in self.lstOfBodies:
			dist = sys.maxint
			closest = None
			for j in self.centroids.keys():
				x = i.distanceTo(j)
				if (x<dist):
					dist = x
					closest = j

			toAddTo = self.centroids.get(closest)
			if (toAddTo != None):
				toAddTo.append(i)
			else:
				print "ERROR: Shouldn't have reached here"

	def recluster(self, numIterations):
		raise NotImplementedError("Please Implement this method")


#Serial Implementation of clustering of Nodes on 2-D plane
class NodeWorld(World):
	def recluster(self, numIterations):
		for i in xrange(numIterations):
			newCentroids = {}
			for c in self.centroids.keys():
				bodies = self.centroids.get(c)
				x=0
				y=0
				for body in bodies:
					x += body.getX()
					y += body.getY()
				x = x/(len(bodies))
				y = y/(len(bodies))
				newC = Node(x,y)
				newCentroids[newC] = []

			self.centroids = newCentroids
			self.setBodies()

#Serial Implementation of cluster of DNA
class DNAWorld(World):
	def recluster(self, numIterations):
		i = 0
		avgCentMoveDist = sys.maxint
		threshold = 0.1
		
		while (i<numIterations and avgCentMoveDist > threshold):
			newCentroids = {}
			avgCentMoveDist = 0
			for c in self.centroids.keys():
				newC = ""
				bodies = self.centroids.get(c)
				for i in xrange(len(c.getDNA())):
					aCnt = 0
					cCnt = 0
					gCnt = 0
					tCnt = 0
					for j in bodies:
						base = j.getDNA()[i]
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
				newCent = DNA(newC)
				assert (len(newCent.getDNA()) == len(c.getDNA()))
				#increase the amount all the centroids moved
				avgCentMoveDist += newCent.distanceTo(c)
				newCentroids[newCent] = []
			#average out total distance moved with number of centroids
			avgCentMoveDist = avgCentMoveDist / self.numCentroids
			self.centroids = newCentroids
			self.setBodies()
			i+=1


if __name__ == "__main__":
	BG = BodyGenerator(0, 10, 0, 10)
	numNodes = 10000
	numCentroids = 2
	NW = NodeWorld(BG.generateRandomNodes(numNodes), numCentroids)
	BG.printNodes(NW.centroids.keys())
	numIterations = 40
	NW.recluster(numIterations)
	BG.printNodes(NW.centroids.keys())
