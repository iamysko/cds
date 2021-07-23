export const SESSION_LOGIN = "SESSION_LOGIN";
export const SESSION_LOGOUT = "SESSION_LOGOUT";

export const sessionLogin = (token: string) => (dispatch: any) => dispatch({
    type: SESSION_LOGIN,
    payload: token
});
