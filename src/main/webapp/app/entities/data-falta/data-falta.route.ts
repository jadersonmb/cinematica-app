import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDataFalta, DataFalta } from 'app/shared/model/data-falta.model';
import { DataFaltaService } from './data-falta.service';
import { DataFaltaComponent } from './data-falta.component';
import { DataFaltaDetailComponent } from './data-falta-detail.component';
import { DataFaltaUpdateComponent } from './data-falta-update.component';

@Injectable({ providedIn: 'root' })
export class DataFaltaResolve implements Resolve<IDataFalta> {
  constructor(private service: DataFaltaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataFalta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dataFalta: HttpResponse<DataFalta>) => {
          if (dataFalta.body) {
            return of(dataFalta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DataFalta());
  }
}

export const dataFaltaRoute: Routes = [
  {
    path: '',
    component: DataFaltaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.dataFalta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DataFaltaDetailComponent,
    resolve: {
      dataFalta: DataFaltaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.dataFalta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DataFaltaUpdateComponent,
    resolve: {
      dataFalta: DataFaltaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.dataFalta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DataFaltaUpdateComponent,
    resolve: {
      dataFalta: DataFaltaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.dataFalta.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
