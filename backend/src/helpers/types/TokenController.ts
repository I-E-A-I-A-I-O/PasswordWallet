import {TokenPayload} from './TokenPayload';

export abstract class TokenController {
  abstract invalidToken(token: string): Promise<boolean>;
  abstract getPayload(token: string | undefined): Promise<TokenPayload | null>;
  abstract invalidateToken(token: string): Promise<boolean>;
}
