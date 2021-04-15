import random

def listSub_(target,dataSet,path,min_):
    if len(dataSet)==0:
        return path,min_
    
    for x in dataSet:
        t=target-x
        print(target,"-",x,"=",t)
        af=list(filter(lambda it:it<=t,dataSet))
        print(t,af,path+[x],min_)
        # p,m=listSub_(t,af,path+[x],min_)
        # print(p,m)


def listSub(target,dataSet):
    return listSub_(target,dataSet,[],0)
    
        # return path,min

    # minRest=-1
    # for i in range(0,len(dataSetFiltered)):
    #     pivot=dataSetFiltered[i]
    #     tmpList=[pivot]
    #     for e in range(i+1,len(dataSetFiltered)):
    #         tmpPivot=dataSetFiltered[e]
    #         summatory=sum(tmpList)+tmpPivot
    #         rest=target-summatory
    #         print(tmpList,tmpPivot,"->",summatory,rest)
    #         # if summatory>target:
    #         #     break
    #         if summatory<target:
    #             tmpList.append(tmpPivot)
    #         #     # if rest >0:
    #         #     #     if minRest==-1:
    #         #     #         minRest=rest
    #         #     #     else:
    #         #     #         if rest < minRest:
    #         #     #             minRest=rest
    #         # elif summatory==target:
    #         #     tmpList.append(tmpPivot)
    #         #     print("->",tmpList,"=",target)
    #         #     lists.append(list(tmpList))
    #         #     tmpList.pop()

    return lists

if __name__ == "__main__":
    size=100000
    limitMin=0
    limitMax=40

    target=153
    # dataSet=[random.randint(limitMin,limitMax) for x in range(0,size)]
    # dataSet=[36, 21, 8, 12, 33]
    dataSet=[70,100,20,50,120,80]
    # print(dataSet)
    print(listSub(target,dataSet))
