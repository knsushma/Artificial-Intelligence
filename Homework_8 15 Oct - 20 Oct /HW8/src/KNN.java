import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A kNN classification algorithm implementation.
 * 
 */

public class KNN {	
	/**
	 * In this method, you should implement the kNN algorithm. You can add 
	 * other methods in this class, or create a new class to facilitate your
	 * work. If you create other classes, DO NOT FORGET to include those java
   * files when preparing your code for hand in.
   *
	 * Also, Please DO NOT MODIFY the parameters or return values of this method,
   * or any other provided code.  Again, create your own methods or classes as
   * you need them.
	 * 
	 * @param trainingData
	 * 		An Item array of training data
	 * @param testData
	 * 		An Item array of test data
	 * @param k
	 * 		The number of neighbors to use for classification
	 * @return
	 * 		The object KNNResult contains classification accuracy, 
	 * 		category assignment, etc.
	 */
	
	class EuclidianItem {
		public String name;
		public String category;
		public double euclidianDistance;	
		public EuclidianItem(String category, String name, double spaceDistance) {
			this.category = category;
			this.name = name;
			this.euclidianDistance = spaceDistance;
		}
		public String getName() {
			return this.name;
		}
	}
	
	
	public KNNResult classify(Item[] trainingData, Item[] testData, int k) {
		KNNResult knnResult = new KNNResult();
		knnResult.nearestNeighbors = new String[testData.length][k];
		knnResult.categoryAssignment = new String[k];
		String[] catagories = new String[testData.length];
		int accuracyCount = 0;
		for (int i=0; i<testData.length; i++) {			
			List<EuclidianItem> kNearestNeighbor = findKNearestNeighbour(trainingData, testData[i], k);
			catagories[i] = predicateCatagory(kNearestNeighbor);	
			if (testData[i].category.equalsIgnoreCase(catagories[i])) {
				accuracyCount += 1;
			}
			kNearestNeighbor.stream().map(item -> item.getName()).collect(Collectors.toList()).toArray(knnResult.nearestNeighbors[i]);
		}
		knnResult.categoryAssignment = catagories;
		knnResult.accuracy = (double) accuracyCount /  (double)testData.length;
		return knnResult;
	}
	
	
	public List<EuclidianItem> findKNearestNeighbour(Item[] trainingData, Item testData, int k) {			
		int numberOfTrainingItems = trainingData.length;
		List<EuclidianItem> items = new ArrayList<EuclidianItem>();		
		for (int i=0; i<numberOfTrainingItems; ++i) {
			items.add(new EuclidianItem(trainingData[i].category, trainingData[i].name, findEuclidianDistance(testData, trainingData[i])));
		}
		Collections.sort(items, new Comparator<EuclidianItem>() {
			public int compare(EuclidianItem item1, EuclidianItem item2) {
				if (item1.euclidianDistance > item2.euclidianDistance)
					return 1;
				else if (item1.euclidianDistance < item2.euclidianDistance)
					return -1;
				else
					return 0;
			}
		});
		return items.subList(0, k);	
	}
	
	
	public double findEuclidianDistance(Item item1, Item item2) {	
		double euclidianDistance = 0;		
		double xDimDistance = Math.pow(item1.features[0]-item2.features[0],2);
		double yDimDistance = Math.pow(item1.features[1]-item2.features[1],2);
		double zDimDistance = Math.pow(item1.features[2]-item2.features[2],2);	
		euclidianDistance = Math.sqrt(xDimDistance+yDimDistance+zDimDistance);	
		return euclidianDistance;
	}

	
	public String predicateCatagory(List<EuclidianItem> kNearestNeighbor) {
		int machineCatagory = 0, fruitCatagory = 0, nationCatagory = 0;
		for (EuclidianItem item : kNearestNeighbor) {
			if ("fruit".equalsIgnoreCase(item.category)) {
				fruitCatagory += 1;
			} else if ("nation".equalsIgnoreCase(item.category)) {
				nationCatagory += 1;
			} else {
				machineCatagory += 1;
			}
		}	
		if ( (nationCatagory >= machineCatagory) && (nationCatagory >= fruitCatagory) ) {
			return (new String("nation"));
		} else if ( machineCatagory >= fruitCatagory ) {
			return (new String("machine"));
		} else {
			return (new String("fruit"));
		}
	}	
	
}
