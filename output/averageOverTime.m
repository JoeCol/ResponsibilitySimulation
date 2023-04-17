naiveAverageOverTime = zeros(5,20000);
resAverageOverTime = zeros(5,20000);

naiveBadOverTime = zeros(5,20000);
resBadOverTime = zeros(5,20000);

for cn = 1:size(dirtAppearence,2)
    tmp = naive(naive(:,2)==dirtAppearence(cn),[1,4]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    naiveAverageOverTime(cn,:) = ((mean(toShow,2)./possibleDirtLocations)*100);
    
    tmp = naive(naive(:,2)==dirtAppearence(cn),[1,5]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    naiveBadOverTime(cn,:) = ((mean(toShow,2)./possibleDirtLocations)*100);

    tmp = res(res(:,2)==dirtAppearence(cn),[1,4]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    resAverageOverTime(cn,:) = ((mean(toShow,2)./possibleDirtLocations)*100);
    
    tmp = res(res(:,2)==dirtAppearence(cn),[1,5]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    resBadOverTime(cn,:) = ((mean(toShow,2)./possibleDirtLocations)*100);
end

naiveAverageOverTime = naiveAverageOverTime';
resAverageOverTime = resAverageOverTime';

naiveBadOverTime = naiveBadOverTime';
resBadOverTime = resBadOverTime';