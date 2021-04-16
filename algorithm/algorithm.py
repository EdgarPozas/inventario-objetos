import random
# if len(dataSet)==0:
#     # print("->",path,originalTarget-sum(path))
#     k=abs(originalTarget-sum(path))
#     if not k in ends.keys():
#         ends[k]=[]
#     ls=list(path)
#     ls.sort()
#     if not ls in ends[k]:
#         ends[k].append(ls)
#     return

# for i in range(0,len(dataSet)):
#     value=dataSet[i]
#     arr=dataSet[0:i]+dataSet[i+1:]
#     t=target-value
#     arrF=list(filter(lambda it:it<=t,arr))
#     path=path+[value]
#     # print(target,"-",value,"=",t,"\t",arr,"<="+str(t),arrF,path)
    
#     listSub(originalTarget,t,arrF,path)
#     p=path.pop()
#     # print("pop",p)
#     # print(path)



    # stack=[]
    # stackDs=[]
    # end=False
    # i=0
    # ds=dataSet 
    # p=[]
    # paths={}

    # # target=153
    # # ds=[160,120,100,80,70,70,50,20]
    # while not end:
    #     pivot=ds[i]                                
    #     t=target-pivot                              
    #     if t<0:                                     
    #         i+=1                                    
    #         continue    
    #     p+=[pivot]
    #     nextV=i+1
    #     if len(ds)>nextV:
    #         if not t in paths.keys():
    #             paths[t]=[]
    #         paths[t].append(p)
    #         i=stack.pop()+1
    #         ds=stackDs.pop()
    #         continue
    #     arr=ds[i+1:]
    #     ops=list(filter(lambda it:it<=t,arr))
    #     stack.append(i)
    #     stackDs.append(arr)
    #     i=0
    #     ds=ops
    #     target=t


ends={}

def listSubNoOptimal(originalTarget,target,dataSet,path):
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
        print(target,"-",value,"=",t,"\t",arr,"<="+str(t),arrF,path)
        
        listSub(originalTarget,t,arrF,path)
        # p=path.pop()
        # print("pop",p)
        # print(path)

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
        if t<0:
            continue
        arr=dataSet[i+1:]
        ops=list(filter(lambda it:it<=t,arr))
        listSub(originalTarget,t,ops,path+[dataSet[i]])



if __name__ == "__main__":
    size=10
    limitMin=0
    limitMax=40

    target=153
    # dataSet=[random.randint(limitMin,limitMax) for x in range(size)]
    dataSet=[70,100,20,70,50,120,80,160]
    dataSet.sort(reverse=True)
    # listSub(target,target,dataSet,[])
    listSubNoOptimal(target,target,dataSet,[])
    # print(ends)