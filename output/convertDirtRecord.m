function dirtRecord = convertDirtRecord(filename,simTime,runNumber,dirtAppearenceRate)

dirtRecord = zeros(simTime,5);
dirtRecord(:,3:5) = importDirtRecord(filename);
dirtRecord(:,1) = runNumber;
dirtRecord(:,2) = dirtAppearenceRate;

end