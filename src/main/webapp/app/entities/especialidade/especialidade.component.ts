import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from './especialidade.service';
import { EspecialidadeDeleteDialogComponent } from './especialidade-delete-dialog.component';

@Component({
  selector: 'jhi-especialidade',
  templateUrl: './especialidade.component.html'
})
export class EspecialidadeComponent implements OnInit, OnDestroy {
  especialidades?: IEspecialidade[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected especialidadeService: EspecialidadeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.especialidadeService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IEspecialidade[]>) => (this.especialidades = res.body ? res.body : []));
      return;
    }
    this.especialidadeService.query().subscribe((res: HttpResponse<IEspecialidade[]>) => {
      this.especialidades = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEspecialidades();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEspecialidade): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEspecialidades(): void {
    this.eventSubscriber = this.eventManager.subscribe('especialidadeListModification', () => this.loadAll());
  }

  delete(especialidade: IEspecialidade): void {
    const modalRef = this.modalService.open(EspecialidadeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.especialidade = especialidade;
  }
}
