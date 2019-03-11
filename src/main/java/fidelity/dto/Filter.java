package fidelity.dto;

public class Filter {
	FilterType type;
	String value;
	
	public enum FilterType {
		security,
		fund,
		all;
	}

	public Filter (FilterType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
}
