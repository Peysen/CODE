package com.java.data.dataStru;

import java.util.Scanner;

/**
 * Created by prayyic on 2017/6/27.
 */
class DATA2{
    String key;
    String name;
    int age;
}

class CLType{
    DATA2 dataNode;
    CLType nextNode;

    CLType CLAddEnd(CLType head,DATA2 dataNode){//追加节点
        CLType node;//存放节点
        CLType htmpt;//临时节点；
        node =new CLType();
        if(node==null){
            System.out.println("节点分配内存失败！");
        }else{
            node.dataNode=dataNode;
            node.nextNode=null;

            if(head==null){
                head=node;
                return head;
            }
            htmpt=head;
            while(htmpt.nextNode!=null){
                htmpt=htmpt.nextNode;
            }
            htmpt.nextNode=node;
        }
        return head;//返回头节点
    }

    CLType CLAddHead(CLType head,DATA2 dataNode){//在头结点后插入节点
        CLType node;
        node=new CLType();
        if(node==null){
            System.out.println("新节点分配内存失败！");
            return head;
        }else{
            node.nextNode=head.nextNode;
            head.nextNode=node;
            return head;//返回头结点
        }
    }

    //查找节点
    CLType CLFindNode(CLType head,String key){
        CLType htemp;//存放临时节点
        htemp=head;
        while(htemp!=null){
            if(htemp.dataNode.key.compareTo(key)==0){
                return htemp;
            }
            htemp=htemp.nextNode;
        }
        return null;
    }

    //指定位置插入节点
    CLType CLInsertNode(CLType head,String findKey,DATA2 dataNode){
        CLType node;//新插入的节点
        CLType htemp;//存放临时节点

        node=new CLType();
        if(node==null){
            System.out.println("新节点分配内存失败！");
            return head;
        }else{
            node.dataNode=dataNode;
            htemp=head;
            while(htemp!=null){
                if(htemp.dataNode.key.compareTo(findKey)==0){
                    node.nextNode=htemp.nextNode;
                    htemp.nextNode=node;
                    return head;
                }
                htemp=htemp.nextNode;
            }
        }
        return null;
    }

    //删除节点
    int CLDelNode(CLType head,String key){
        CLType node;//删除节点的前一节点
        CLType htemp;//所要删除的节点

        htemp=head;
        node=head;
        while(htemp!=null){
            //查找所要删除节点
            if(htemp.dataNode.key.compareTo(key)==0){
                node.nextNode=htemp.nextNode;
                htemp=null;
                return 1;
            }else{
                node=htemp;//指向当前节点
                htemp=htemp.nextNode;//指定下一节点
            }
        }
        return 0;
    }

    //计算链表长度
    int CLLength(CLType head){
        CLType htemp;//存放临时节点
        htemp=head;
        int len=0;
        while(htemp!=null){
            len++;
            htemp=htemp.nextNode;
        }
        return len;
    }

    //显示所有节点
    void CLAllNode(CLType head){
        CLType htemp;//存放临时节点
        DATA2 data2;
        htemp=head;
        System.out.printf("当前链表共有%d个节点,链表数据如下：\n",CLLength(head));
        while(htemp!=null){
            data2=htemp.dataNode;
            System.out.printf("该节点信息为：(%s,%s,%d)\n",data2.key,data2.name,data2.age);
            htemp=htemp.nextNode;
        }
    }
}

public class cp_2 {
    public static void main(String[] args) {
        CLType node,head=null;
        CLType cl=new CLType();
        String key,findKey;
        System.out.println("顺序表操作演示！");


        Scanner input=new Scanner((System.in));

        do{
            System.out.println("链表测试，输入添加节点的数据（学号，姓名，年龄）");
            DATA2 nodeData=new DATA2();
            nodeData.key=input.next();
            nodeData.name=input.next();
            nodeData.age=input.nextInt();

            if(nodeData.key.equals("0")){
                break;
            }else{
                head=cl.CLAddEnd(head,nodeData);
            }
        }while(true);
        cl.CLAllNode(head);

        System.out.println("演示插入节点，输入插入节点的关键字：");
        findKey=input.next();
        DATA2 insertNode=new DATA2();
        insertNode.key="006";
        insertNode.name="ff";
        insertNode.age=66;
        cl.CLInsertNode(head,findKey,insertNode);
        cl.CLAllNode(head);

        System.out.println("演示删除节点，输入删除节点的关键字：");
        key=input.next();
        cl.CLDelNode(head,key);
        cl.CLAllNode(head);

        System.out.println("演示查找节点，输入查找节点的关键字：");
        key=input.next();
        node = cl.CLFindNode(head, key);
        System.out.printf("关键字%s对应的节点是（%s,%s,%d）\n",
                key,node.dataNode.key,key,node.dataNode.name,key,node.dataNode.age);

    }
}
