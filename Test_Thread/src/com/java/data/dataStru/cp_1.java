package com.java.data.dataStru;

import java.util.Scanner;

/**
 * Created by prayyic on 2017/6/9.
 */

class DATA{
    String key;
    String name;
    int age;
}

class SLType{
    static final int MAXLEN=100;
    DATA[] ListData=new DATA[MAXLEN+1];
    int  ListLen;

    void SLInit(SLType sl){//初始化顺序表
        sl.ListLen=0;
    }

    int SLLength(SLType sl){
        return sl.ListLen;
    }

    int SLInsert(SLType sl,int i,DATA data){//指定位置插入数据
        int n;
        if(sl.ListLen>MAXLEN) {
            System.out.println("顺序表已满，不能再插入数据!");
            return 0;
        }
        if(i<1||i>sl.ListLen-1){
            System.out.println("插入元素的顺序错误，不能插入数据!");
            return 0;
        }
        for(n=sl.ListLen;n>=i;n--){
            sl.ListData[n+1]=sl.ListData[n];
        }
        sl.ListData[n]=data;
        sl.ListLen++;
        return 1;
    }

    int SLAdd(SLType sl,DATA data){//添加数据至顺序表尾部
        if(sl.ListLen>MAXLEN){
            System.out.println("顺序表已满!");
            return 0;
        }
        sl.ListData[++sl.ListLen]=data;
        return 1;
    }

    int SLdelete(SLType sl,int n){//删除节点
        if(n<1||n>sl.ListLen){
            System.out.println("所删节点序号错误！");
            return 0;
        }
        for(int i=n;i<sl.ListLen;i++){
            sl.ListData[n]=sl.ListData[n+1];
        }
        sl.ListLen--;
        return 1;
    }

    DATA SLFindByNum(SLType sl,int n){//通过序号查找节点
        if(n<1||n>sl.ListLen){
            System.out.println("所删节点序号错误！");
            return null;
        }
        return sl.ListData[n];
    }

    int SLFindByCont(SLType sl,String key){//通过关键字查找节点
        int i;
        for(i=1;i<sl.ListLen;i++){
            if(sl.ListData[i].key.compareTo(key)==0){
                return i;
            }
        }
        return 0;
    }

    int SLAll(SLType sl){
        for(int i=1;i<=sl.ListLen;i++){
            System.out.printf("(%s,%s,%d)\n",
                    sl.ListData[i].key,sl.ListData[i].name,sl.ListData[i].age);
        }
        return 0;
    }
}

public class cp_1 {
    public static void main(String[] args) {
        int i;
        SLType sl=new SLType();
        DATA pdata;
        String key;
        System.out.println("顺序表操作演示！");

        sl.SLInit(sl);
        System.out.println("初始化完成！");

        Scanner input=new Scanner((System.in));

        do{
            System.out.println("输入添加节点的数据（学号，姓名，年龄）");
            DATA data=new DATA();
            data.key=input.next();
            data.name=input.next();
            data.age=input.nextInt();

            if(data.age!=0){
             if(sl.SLAdd(sl,data)==0){
                 break;
             }
            }else{
                break;
            }
        }while(true);

        System.out.println("顺序表中的节点顺序为：");
        sl.SLAll(sl);

        System.out.printf("取出指定节点的序号:");
        i=input.nextInt();
        pdata=sl.SLFindByNum(sl,i);
        if(pdata!=null){
            System.out.printf("第%d个节点为：（%s,%s,%d）\n",i,pdata.key,pdata.name,pdata.age);
        }
    }
}
