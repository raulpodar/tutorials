package com.baeldung.grpc.server;

import com.grpc.multiply.Request;
import com.grpc.multiply.Response;
import com.grpc.multiply.Blocks;
import com.grpc.multiply.MultiplyServiceGrpc.MultiplyServiceImplBase;

import io.grpc.stub.StreamObserver;

public class MatrixMultiplyImpl extends MultiplyServiceImplBase {
	
	private static final int MAX=4;

 
    
    public static int[][] multiplyBlock(int A[][], int B[][]){
    	int C[][]= new int[MAX][MAX];
    	C[0][0]=A[0][0]*B[0][0]+A[0][1]*B[1][0];
    	C[0][1]=A[0][0]*B[0][1]+A[0][1]*B[1][1];
    	C[1][0]=A[1][0]*B[0][0]+A[1][1]*B[1][0];
    	C[1][1]=A[1][0]*B[0][1]+A[1][1]*B[1][1];
    	return C;
    	
    	
    }
    
    public static int[][] addBlock(int A[][], int B[][]){
    	int C[][]= new int[MAX][MAX];
    	for (int i=0;i<C.length;i++)
    	{
    		for (int j=0;j<C.length;j++)
    		{
    			C[i][j]=A[i][j]+B[i][j];
    		}
    	}
    	return C;
    }
    
    @Override
    public void multiplyBlock(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("Request received from client:\n" + request);

    	int [][] arrayA=makeArray(request.getA());
    	int[][] arrayB=makeArray(request.getB());
    	int [][] newArray=multiplyBlock(arrayA,arrayB);
    	
        Response response=Response.newBuilder()
        		.setC(makeBlocks(newArray))
        		.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void addBlock(Request request, StreamObserver<Response> responseObserver) {
        
    	System.out.println("Request received from client:\n" + request);
    	int [][] arrayA=makeArray(request.getA());
    	int[][] arrayB=makeArray(request.getB());
    	int [][] newArray=addBlock(arrayA,arrayB);
    	
        Response response=Response.newBuilder()
        		.setC(makeBlocks(newArray))
        		.build();
       

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    public static int [][] makeArray(Blocks request){
    	int [][] array=new int[MAX][MAX];
    	
    	array[0][0]=request.getA1();
    	array[0][1]=request.getB1();
    	array[1][0]=request.getC1();
    	array[1][1]=request.getD1();
    	
    	return array;
    	
    }
    
    public Blocks makeBlocks(int[][] array)
    {
    	Blocks C= Blocks.newBuilder()
    			.setA1(array[0][0])
    			.setB1(array[0][1])
    			.setC1(array[1][0])
    			.setD1(array[1][1])
    			.build();
    	
    	return C;
    }
}
