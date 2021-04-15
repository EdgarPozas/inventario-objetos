import random

ends={}

def listSub(originalTarget,target,dataSet,path):
    if len(dataSet)==0:
        # print("->",path,originalTarget-sum(path))
        k=abs(originalTarget-sum(path))
        if not k in ends.keys():
            ends[k]=[]
        ls=list(path)
        ls.sort()
        if not ls in ends[k]:
            ends[k].append(ls)
        return

    for i in range(0,len(dataSet)):
        value=dataSet[i]
        arr=dataSet[0:i]+dataSet[i+1:]
        t=target-value
        arrF=list(filter(lambda it:it<=t,arr))
        path=path+[value]
        # print(target,"-",value,"=",t,"\t",arr,"<="+str(t),arrF,path)
        
        listSub(originalTarget,t,arrF,path)
        p=path.pop()
        # print("pop",p)
        # print(path)

if __name__ == "__main__":
    size=10
    limitMin=0
    limitMax=40

    target=153
    dataSet=[random.randint(limitMin,limitMax) for x in range(0,size)]
    # dataSet=[36, 21, 8, 12, 33]
    # dataSet=[70,100,20,70,50,120,80,160]
    # print(dataSet)
    listSub(target,target,dataSet,[])
    print(ends)
    # min_=-1
    # for x in ends:
    #     if min_==-1:
    #         min_=x["rest"]
    #     else:
    #         if x["rest"]<min_:
    #             min_=x["rest"]
        
    # subs=list(filter(lambda x: x["rest"]==min_,ends))
    # print(subs)