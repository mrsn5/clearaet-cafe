import { HttpClient } from '@angular/common/http';
import {environment} from "../../environments/environment";
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ApiClientService {
    private basePath = environment.apiUrl + '/';
    constructor(private httpClient: HttpClient) { }

    public get<T>(url:string) : Observable<T> {
        return this.httpClient.get<T>(this.basePath + url);
    }

    public post<T>(url:string, payload: any) : Observable<T> {
        return this.httpClient.post<T>(this.basePath + url, payload);
    }

    public put<T>(url:string, payload: any) : Observable<T> {
        return this.httpClient.put<T>(this.basePath + url, payload);
    }

    public delete<T>(url:string, payload: any) : Observable<T> {
        return this.httpClient.delete<T>(this.basePath + url);
    }
}
