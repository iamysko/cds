import { SESSION_LOGIN } from "../actions/sessionActions";


export default function (state = {}, action: any) {
    console.log("session reducer!");
    
    switch (action.type) {
        case SESSION_LOGIN:
            console.log("Logging in with reducer!");
            
            return {
                ...state,
                ...action.payload
            };
        
        default:
            return state;
    }
};
