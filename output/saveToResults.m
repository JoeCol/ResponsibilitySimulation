saveLocation = "/Users/user/Documents/GitHub/ResECAI23/results/";

writematrix(naiveAverageOverTime(:,1), strcat(saveLocation,"naiveAverage1.csv"));
writematrix(naiveAverageOverTime(:,2), strcat(saveLocation,"naiveAverage2.csv"));
writematrix(naiveAverageOverTime(:,3), strcat(saveLocation,"naiveAverage3.csv"));
writematrix(naiveAverageOverTime(:,4), strcat(saveLocation,"naiveAverage4.csv"));
writematrix(naiveAverageOverTime(:,5), strcat(saveLocation,"naiveAverage5.csv"));
writematrix(naiveBadOverTime, strcat(saveLocation,"naiveBadAverage.csv"));
writematrix(resAverageOverTime, strcat(saveLocation,"resAverage.csv"));
writematrix(resBadOverTime, strcat(saveLocation,"resBadAverage.csv"));