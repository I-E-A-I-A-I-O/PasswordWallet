import {Users} from './Users';
import {Passwords} from './Passwords';
import {UserSession} from './Session';

const users = new Users();
const passwords = new Passwords();
const session = new UserSession();

export {users, passwords, session};
