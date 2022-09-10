package com.hytechcoder.dscatalog.services.exceptions;

public class DatabaseException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		public DatabaseException(String smg) {
			super(smg);
		}
}
