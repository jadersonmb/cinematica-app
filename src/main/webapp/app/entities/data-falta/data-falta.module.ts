import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { DataFaltaComponent } from './data-falta.component';
import { DataFaltaDetailComponent } from './data-falta-detail.component';
import { DataFaltaUpdateComponent } from './data-falta-update.component';
import { DataFaltaDeleteDialogComponent } from './data-falta-delete-dialog.component';
import { dataFaltaRoute } from './data-falta.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(dataFaltaRoute)],
  declarations: [DataFaltaComponent, DataFaltaDetailComponent, DataFaltaUpdateComponent, DataFaltaDeleteDialogComponent],
  entryComponents: [DataFaltaDeleteDialogComponent]
})
export class CinematicaDataFaltaModule {}
