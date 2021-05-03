import logger from './logger';
import {Encryption} from './encryption';
import {DatabaseConnection} from './db';
import queries from './query';

const log = new logger();
const rsa = new Encryption();
const db = new DatabaseConnection();

export {log, rsa, db, queries};
