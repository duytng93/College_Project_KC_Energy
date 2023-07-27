package kc_energy_v2;

public class Accounts {
	private int accountNumber;
	private String name, address, phoneNumber, plan, lastPaymentDate;
	private double remainingDue, lastPayment, currentUsage, lastUsage;
	
	Accounts(){}

	Accounts(int accountNumber, String name, String address, String phoneNumber, String plan) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.plan = plan;
		this.remainingDue = 0;
		this.lastPayment = 0;
		this.lastPaymentDate = "1900-01-01";
		this.currentUsage = 0;
		this.lastUsage = 0;
	}
	
	public Accounts(int accountNumber, String name, String address, String phoneNumber, String plan,
			 double remainingDue, double lastPayment,String lastPaymentDate, double currentUsage, double lastUsage) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.plan = plan;
		this.lastPaymentDate = lastPaymentDate;
		this.remainingDue = remainingDue;
		this.lastPayment = lastPayment;
		this.currentUsage = currentUsage;
		this.lastUsage = lastUsage;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public double getRemainingDue() {
		return remainingDue;
	}

	public void setRemainingDue(double remainingDue) {
		this.remainingDue = remainingDue;
	}

	public double getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(double lastPayment) {
		this.lastPayment = lastPayment;
	}

	public double getCurrentUsage() {
		return currentUsage;
	}

	public void setCurrentUsage(double currentUsage) {
		this.currentUsage = currentUsage;
	}

	public double getLastUsage() {
		return lastUsage;
	}

	public void setLastUsage(double lastUsage) {
		this.lastUsage = lastUsage;
	}
	
}
