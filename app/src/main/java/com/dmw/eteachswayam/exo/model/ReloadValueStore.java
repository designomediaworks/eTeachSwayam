package com.dmw.eteachswayam.exo.model;

public class ReloadValueStore {
	String V_Board;
	String V_Medium;
	String V_Class;
	String Dt_ActDate;
	String N_CalCounter;
	String V_CouponNo;
	String V_CouponRefNo;
	@Override
	public
    String toString() {
		return V_Board+"/"+V_Medium+"/"+V_Class+"/";
	}
	public ReloadValueStore() {

	}

	public ReloadValueStore( String v_Board, String v_Medium, String v_Class,
                             String dt_ActDate, String n_CalCounter, String v_CouponNo, String V_CouponRefNo){
		super();
		V_Board = v_Board;
		V_Medium = v_Medium;
		V_Class = v_Class;
		Dt_ActDate = dt_ActDate;
		N_CalCounter = n_CalCounter;
		V_CouponNo=v_CouponNo;
		this.V_CouponRefNo=V_CouponRefNo;
	}

	public
    String getV_CouponRefNo() {
		return V_CouponRefNo;
	}
	public void setV_CouponRefNo(String v_CouponRefNo) {
		V_CouponRefNo = v_CouponRefNo;
	}
	public
    String getV_CouponNo() {
		return V_CouponNo;
	}
	public void setV_CouponNo(String v_CouponNo) {
		V_CouponNo = v_CouponNo;
	}
	public
    String getV_Board() {
		return V_Board;
	}

	public void setV_Board(String v_Board) {
		V_Board = v_Board;
	}

	public
    String getV_Medium() {
		return V_Medium;
	}

	public void setV_Medium(String v_Medium) {
		V_Medium = v_Medium;
	}

	public
    String getV_Class() {
		return V_Class;
	}

	public void setV_Class(String v_Class) {
		V_Class = v_Class;
	}

	public
    String getDt_ActDate() {
		return Dt_ActDate;
	}

	public void setDt_ActDate(String dt_ActDate) {
		Dt_ActDate = dt_ActDate;
	}

	public
    String getN_CalCounter() {
		return N_CalCounter;
	}

	public void setN_CalCounter(String n_CalCounter) {
		N_CalCounter = n_CalCounter;
	}

	

}
