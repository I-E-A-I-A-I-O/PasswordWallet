import dotenv from 'dotenv';
dotenv.config();
import express, {Request, Response, NextFunction} from 'express';
import helmet from 'helmet';
import cors from 'cors';
import {log} from './helpers';
import {sessionRouter, usersRouter} from './routers';

const app = express();
const port = process.env.PORT || 8000;

app.use(helmet());
app.use(cors());
app.use(express.json());

app.use('/users', usersRouter);
app.use('/session', sessionRouter);

app.use(
  (
    err: any | undefined,
    request: Request,
    res: Response,
    next: NextFunction
  ) => {
    if (err) {
      log.logger.error(err);
    }
    res.status(400).json({
      error: err.toString(),
    });
  }
);

app.listen(port, () => {
  console.log(`Server running in port ${port}`);
});
