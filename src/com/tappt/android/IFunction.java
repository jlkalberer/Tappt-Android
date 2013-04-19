/**
 * 
 */
package com.tappt.android;

/**
 * @author John
 * A simple interface for running a callback.
 */
public interface IFunction<TType> {
	public void Execute(TType value);
}