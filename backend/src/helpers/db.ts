import {Pool, PoolClient} from 'pg';
import {log} from '../helpers';

export class DatabaseConnection {
  private pool: Pool;

  constructor() {
    this.pool = new Pool({
      connectionString: process.env.DB_URI,
      max: 20,
      min: 1,
      ssl: {
        rejectUnauthorized: false,
      },
      connectionTimeoutMillis: 20000,
    });

    this.pool.on('error', err => {
      log.logger.error(err.message);
    });
  }

  getClient(): Promise<PoolClient> {
    return this.pool.connect();
  }
}
