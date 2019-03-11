package fidelity.dto;

public class Pager {
	long                      offset;
	long                      pagesize;
	long                      totalresults;
	
	public Pager() {
	};
	
	public Pager(long offset, long pagesize) {
		super();
		this.offset = offset;
		this.pagesize = pagesize;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public void setOffset(long offset) {
		this.offset = offset;
	}
	
	public long getPagesize() {
		return pagesize;
	}
	
	public void setPagesize(long pagesize) {
		this.pagesize = pagesize;
	}
	
	public long getTotalresults() {
		return totalresults;
	}
	
	public void setTotalresults(long totalresults) {
		this.totalresults = totalresults;
	}	
}
