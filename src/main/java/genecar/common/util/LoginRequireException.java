package genecar.common.util;

public class LoginRequireException extends Exception {

	private static final long serialVersionUID = 4201713666492394882L;
	
	public LoginRequireException(String errorMessage) {
		super(errorMessage);
	}

}
