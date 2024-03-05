package com.example.demo;

public enum Commands {
START("/start"),
REGISTRATION("Регистрация"),
LOGIN("Вход"),
EXIT("Выход"),
LOGIN_INPUTED(""),
LOGGED(""),
NEAR_LESSON("Ближайшее занятие"),
NEAR_DAY("Следующий учебный день"),
ALL_WEEK("Вся неделя"),
THE_DAY("Выбранный день");
	public final String cmd;
	Commands(String cmd)
	{
		this.cmd=cmd;
	}
	@Override
	public String toString()
	{
		return cmd;
	}
	public boolean equals(String cmd)
	{
		return this.toString().equals(cmd);
	}
}