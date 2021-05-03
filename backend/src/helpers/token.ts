import jwt from 'jsonwebtoken';
import query from './query';
import {db as database, log as logger} from '../helpers';
import {TokenPayload} from './types/TokenPayload';
import {TokenController} from './types/TokenController';

export class Token extends TokenController {
  async invalidToken(token: string): Promise<boolean> {
    let client = await database.getClient();
    try {
      let results = await client.query(query.compareToken, [token]);
      if (results.rowCount < 1) {
        return false;
      }
      return true;
    } catch (err) {
      console.error(err);
      return true;
    } finally {
      client.release(true);
    }
  }

  async getPayload(token: string | undefined): Promise<TokenPayload | null> {
    if (!process.env.JWT_SECRET || !token) {
      return null;
    } else {
      let invalid = await this.invalidToken(token);
      if (invalid) {
        return null;
      } else {
        try {
          const verified = jwt.verify(token, process.env.JWT_SECRET);
          return verified as TokenPayload;
        } catch (error) {
          console.error(error);
          return null;
        }
      }
    }
  }

  async invalidateToken(token: string): Promise<boolean> {
    let client = await database.getClient();
    try {
      await client.query(query.invalidateToken, [token]);
      return true;
    } catch (err) {
      logger.logger.error(err);
      return false;
    } finally {
      client.release(true);
    }
  }
}
