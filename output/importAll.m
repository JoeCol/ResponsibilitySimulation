numOfSimSteps = 20000;

dirtAppearence = 1:5;

numOfRuns = 500;

possibleDirtLocations = 313;

filenameNaive = strcat(pwd,'/Naive/Result/Dirt%d/DirtLevels_%d.csv');
filenameRes = strcat(pwd,'/ResAgents/Result/Dirt%d/DirtLevels_%d.csv');
filenameNaiveA = strcat(pwd,'/Naive/Result/Dirt%d/AliveTime_%d.csv');
filenameResA = strcat(pwd,'/ResAgents/Result/Dirt%d/AliveTime_%d.csv');
naive = zeros(numOfSimSteps*numOfRuns*size(dirtAppearence,2),5);
res = naive;

naiveDirtAlive = zeros(4,0);
resDirtAlive = zeros(4,0);

total = numOfRuns*size(dirtAppearence,2);
current = 1;

startPoint = 1;

w = waitbar(0,'Started processing');
msgToShow = 'run %d of appearance rate %d processing. Done %f%%';
for i = 1:size(dirtAppearence,2)
    for runNumber = 1:numOfRuns
        dirtRate = dirtAppearence(i);

        msg = sprintf(msgToShow,runNumber,dirtAppearence(i),(100 * (current / total)));
        waitbar(current / total,w,msg);
        
        filename = sprintf(filenameNaive,dirtRate,runNumber);
        currentRecord = convertDirtRecord(filename,numOfSimSteps,runNumber,dirtRate);
        naive(startPoint:startPoint+size(currentRecord,1) - 1,:) = currentRecord;
        filename = sprintf(filenameNaiveA,dirtRate,runNumber);
        tmp = importAliveFile(filename,runNumber,dirtRate);
        naiveDirtAlive = [naiveDirtAlive;tmp];

        filename = sprintf(filenameRes,dirtRate,runNumber);
        currentRecord = convertDirtRecord(filename,numOfSimSteps,runNumber,dirtRate);
        res(startPoint:startPoint+size(currentRecord,1) - 1,:) = currentRecord;
        filename = sprintf(filenameResA,dirtRate,runNumber);
        tmp = importAliveFile(filename,runNumber,dirtRate);
        resDirtAlive = [resDirtAlive;tmp];

        startPoint = startPoint + size(currentRecord,1);
        current = current + 1;
    end
end
close(w);
clear filenamef filenameNaiveA filenameResA filename filenameRes filenameNaive current total reverseStr msg tmp startPoint i actualRunNumber currentRecord runNumber;