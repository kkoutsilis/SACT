# Static Analysis Clustering Tool

## Usage

Download the latest release from [here](https://github.com/koutsilis1999/SACT/releases).

Available clustering algorithms:

- [HCNN](https://www.sciencedirect.com/science/article/abs/pii/S0950705121005578?via%3Dihub) with some changes in order to work with the desired input data.

It requires 6 arguments in this order.

1.  path to input csv file.
2.  path to output csv file.
3.  value of k nearest neighbours NN.
4.  first NN algorithm (1-KNN, 2-MKNN, 3-RKNN).
5.  second NN algoruthm (1-KNN, 2-MKNN, 3-RKNN).
6.  number of desired clusters.

Example:

```bash
$ java −jar SACT−1.1−SNAPSHOT.jar ./exp1.csv ./res.csv 3 1 1 2
```

It will print the created clusters in the console as well as create a csv file with the clusters.

### Example input csv file:

| ID  |   SCHEMA   | NAME |   TYPE   |   SCHEMA   | NAME |   TYPE   |
| :-: | :--------: | :--: | :------: | :--------: | :--: | :------: |
|  0  | TestSchema |  10  | TestType | TestSchema |  1   | TestType |
|  1  | TestSchema |  10  | TestType | TestSchema |  2   | TestType |
|  2  | TestSchema |  10  | TestType | TestSchema |  3   | TestType |
|  3  | TestSchema |  10  | TestType | TestSchema |  4   | TestType |
|  4  | TestSchema |  10  | TestType | TestSchema |  5   | TestType |
|  5  | TestSchema |  10  | TestType | TestSchema |  6   | TestType |
|  6  | TestSchema |  10  | TestType | TestSchema |  8   | TestType |
|  7  | TestSchema |  1   | TestType | TestSchema |  2   | TestType |
|  8  | TestSchema |  1   | TestType | TestSchema |  3   | TestType |
|  9  | TestSchema |  1   | TestType | TestSchema |  4   | TestType |
| 10  | TestSchema |  2   | TestType | TestSchema |  3   | TestType |
| 11  | TestSchema |  2   | TestType | TestSchema |  4   | TestType |
| 12  | TestSchema |  2   | TestType | TestSchema |  5   | TestType |
| 13  | TestSchema |  3   | TestType | TestSchema |  4   | TestType |
| 14  | TestSchema |  3   | TestType | TestSchema |  5   | TestType |
| 15  | TestSchema |  4   | TestType | TestSchema |  5   | TestType |
| 16  | TestSchema |  6   | TestType | TestSchema |  7   | TestType |
| 17  | TestSchema |  6   | TestType | TestSchema |  8   | TestType |
| 18  | TestSchema |  6   | TestType | TestSchema |  9   | TestType |
| 19  | TestSchema |  7   | TestType | TestSchema |  9   | TestType |
| 20  | TestSchema |  7   | TestType | TestSchema |  8   | TestType |
| 21  | TestSchema |  7   | TestType | TestSchema |  11  | TestType |
| 22  | TestSchema |  6   | TestType | TestSchema |  11  | TestType |
| 23  | TestSchema |  9   | TestType | TestSchema |  11  | TestType |
| 24  | TestSchema |  8   | TestType | TestSchema |  9   | TestType |
| 25  | TestSchema |  8   | TestType | TestSchema |  6   | TestType |
|  6  | TestSchema |  10  | TestType | TestSchema |  2   | TestType |
| 27  | TestSchema |  9   | TestType | TestSchema |  7   | TestType |
| 28  | TestSchema |  9   | TestType | TestSchema |  8   | TestType |

### Example output file:

| CLUSTER_ID |   SCHEMA   | NAME |   TYPE   |
| :--------: | :--------: | :--: | :------: |
|     0      | TestSchema |  7   | TestType |
|     0      | TestSchema |  6   | TestType |
|     0      | TestSchema |  8   | TestType |
|     0      | TestSchema |  9   | TestType |
|     0      | TestSchema |  11  | TestType |
|     1      | TestSchema |  1   | TestType |
|     1      | TestSchema |  2   | TestType |
|     1      | TestSchema |  3   | TestType |
|     1      | TestSchema |  4   | TestType |
|     1      | TestSchema |  5   | TestType |
|     1      | TestSchema |  10  | TestType |

This tool was developed for my thesis project.
