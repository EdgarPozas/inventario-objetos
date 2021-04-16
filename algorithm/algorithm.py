import random

ends={}

def listSub(originalTarget,target,dataSet,path):
    if dataSet==[]:
        k=abs(originalTarget-sum(path))
        if not k in ends.keys():
            ends[k]=[]
        ls=list(path)
        ls.sort()
        if not ls in ends[k]:
            ends[k].append(ls)
        return
    
    for i in range(0,len(dataSet)):
        t=target-dataSet[i]
        arr=dataSet[i+1:]
        ops=list(filter(lambda it:it<=t,arr))
        p=path+[dataSet[i]]
        listSub(originalTarget,t,ops,p)

dic={}
minR=-1

def listSubDynamic(target,dataSet,path):
    if dataSet==[]:
        print("t",target)
        return True
    for i,v in enumerate(dataSet):
        if target in dic.keys():
            print("in",dic[target],v)
        else:
            res=target-v
            ops=list(filter(lambda it:it<=res,dataSet))
            # print(res)
            r=listSubDynamic(res,ops,path+[res])
            # if not r:
            #     continue
            # print(target,v,res)

            # if not target in dic.keys():
            #     dic[target]={
            #         "target":target,
            #         "next":v,
            #         "res":res
            #     }
            #     print("add",dic[target])
    return False



        # # print("value",v)
        # if v in dic.keys():
        #     print("Found","->",v,dic[v])
        #     pass
        # else:
        #     # print("Not found","->",v)
        #     ops=list(filter(lambda it:it<=v,dataSet))
        #     print(dataSet,"<=",v,ops)
        #     listSubDynamic(v,ops)

        #     if not target in dic.keys():
        #         print("Add value","->",target,v)
        #         dic[target]=[v]
        #     else:
        #         dic[target]+=[v]

    #     print(target,"-",dataSet[i],"=",v,"\t",ops)
    #     if p:
    #         print(p)
    #         # if not v in dic.keys():
    #         #     dic[target]=v
    #         # print("->",path,target,v)
    #         # print(target,p)




if __name__ == "__main__":
    # size=100
    limitMin=0
    limitMax=40

    target=153
    # dataSet=[random.randint(limitMin,limitMax) for x in range(size)]
    dataSet=[70,100,20,50,120,80]
    dataSet=list(filter(lambda x:x<=target,dataSet))
    dataSet.sort(reverse=True)
    # listSub(target,target,dataSet,[])
    listSubDynamic(target,dataSet,[])
    # listSubWhile(target,dataSet)
    # print(ends.keys())