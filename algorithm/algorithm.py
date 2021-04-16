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
        if not path[0] in dic.keys():
            dic[path[0]]=[path]
        else:
            dic[path[0]].append(path)
            
        return 
    for i,v in enumerate(dataSet):
        res=target-v
        if res in dic.keys():
            s=path+dic[res][0]
            dic[res].append(s)
        else:
            ops=list(filter(lambda it:it<=res,dataSet))
            p=path+[res]
            
            listSubDynamic(res,ops,p)
        

      


        # if target in dic.keys():
        #     print("in",dic[target],v)
        # else:

            # print(sum__)
            
           


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
    size=10
    limitMin=0
    limitMax=40

    target=153
    # dataSet=[random.randint(limitMin,limitMax) for x in range(size)]
    # dataSet=[70,100,20,50,120,80]
    dataSet=list(filter(lambda x:x<=target,dataSet))
    dataSet.sort(reverse=True)
    # listSub(target,target,dataSet,[])
    listSubDynamic(target,dataSet,[])
    # print(dic)
    # listSubWhile(target,dataSet)
    # print(ends.keys())