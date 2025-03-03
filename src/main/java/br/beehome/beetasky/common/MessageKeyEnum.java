package br.beehome.beetasky.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKeyEnum {
	SUCCESS("success");
	
	private String msg;
}
