import logger from './logger';
import {Encryption} from './encryption';
import {DatabaseConnection} from './db';
import queries from './query';
import {Token} from './token';

const log = new logger();
const rsa = new Encryption();
const db = new DatabaseConnection();
const token = new Token();

export {token, log, rsa, db, queries};
