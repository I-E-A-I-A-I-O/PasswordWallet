import express, {Request, Response, NextFunction} from 'express';
import helmet from 'helmet';
import cors from 'cors';
import dotenv from 'dotenv';
import {log} from './helpers';
import {usersRouter} from './routers';

dotenv.config();

const app = express();
const port = process.env.PORT || 8000;

app.use(helmet());
app.use(cors());
app.use(express.json());

app.use('/users', usersRouter);

app.use(
  (
    err: any | undefined,
    request: Request,
    res: Response,
    next: NextFunction
  ) => {
    if (err) {
      log.logger.warn(err);
    }
    res.status(500).json({
      error: err,
    });
  }
);

app.listen(port, () => {
  log.logger.info(`Server running in port ${port}`);
});
