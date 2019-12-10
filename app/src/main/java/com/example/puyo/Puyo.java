package com.example.puyo;

public class Puyo {
    private int first;
    private int second;
    private int spin;
    private int[][] pos;

    private int[][] a= {{0,1,0},{0,1,0},{0,0,0}};
    private int[][] b= {{0,0,0},{1,1,0},{0,0,0}};
    private int[][] c= {{0,0,0},{0,1,0},{0,1,0}};
    private int[][] d= {{0,0,0},{0,1,1},{0,0,0}};
    private int[][] error= {{0,0,0},{0,0,0},{0,0,0}};

//    1:f  2: f s  3: s   4: s f 
//      s             f  
    public Puyo(){
        this.first = (int)(Math.random()*100)%4+1;
        this.second = (int)(Math.random()*100)%4+1;
        this.spin = 1;
        this.pos = a;
        this.pos[1][1] = first;
        this.pos[0][1] = second;
    }
    public Puyo(int first, int second){
        this.first = first;
        this.second = second;
        this.spin = 1;
        this.pos = a;
        this.pos[1][1] = first;
        this.pos[0][1] = second;
    }
    public Puyo spin(){
        this.spin = (this.spin)%4+1;
        switch (spin) {
            case 1:{
                this.pos = a;
                this.pos[1][1] = first;
                this.pos[0][1] = second;
                break;
            }
            case 2:{
                this.pos = b;
                this.pos[1][1] = first;
                this.pos[1][0] = second;
                break;
            }
            case 3:{
                this.pos = c;
                this.pos[1][1] = first;
                this.pos[2][1] = second;
                break;
            }
            case 4:{
                this.pos = d;
                this.pos[1][1] = first;
                this.pos[1][2] = second;
                break;
            }
        }
        return this;
    }
    public Puyo spin(int spinnum){
        this.spin = spinnum;
        switch (spinnum) {
            case 1:{
                this.pos = a;
                this.pos[1][1] = first;
                this.pos[0][1] = second;
                break;
            }
            case 2:{
                this.pos = b;
                this.pos[1][1] = first;
                this.pos[1][0] = second;
                break;
            }
            case 3:{
                this.pos = c;
                this.pos[1][1] = first;
                this.pos[2][1] = second;
                break;
            }
            case 4:{
                this.pos = d;
                this.pos[1][1] = first;
                this.pos[1][2] = second;
                break;
            }
        }
        return this;
    }
    public int Getfirst(){
        return this.first;
    }
    public int Getsecond(){
        return this.second;
    }
    public int Getspin(){
        return this.spin;
    }
    public int[][] Getpos(){

        return this.pos;
    }
    public int[][] Getspin_res(){
        switch ((spin+1)%4) {
            case 1:{
                return this.a;
            }
            case 2:{
                return this.b;
            }
            case 3:{
                return this.c;
            }
            case 4:{
                return this.d;
            }
        }
		return this.error;
    }
}