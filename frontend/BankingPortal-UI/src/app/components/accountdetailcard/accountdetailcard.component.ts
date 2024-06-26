import { Component, OnInit } from '@angular/core';
import { ToastService } from 'angular-toastify';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-accountdetailcard',
  templateUrl: './accountdetailcard.component.html',
  styleUrls: ['./accountdetailcard.component.css']
})
export class AccountdetailcardComponent implements OnInit {
  accountDetails: any;

  constructor(private apiService: ApiService , private _toastService: ToastService ) { }

  ngOnInit(): void {
    this.getAccountDetails();
  }

  getAccountDetails(): void {
    this.apiService.getAccountDetails().subscribe(
      (data: any) => {
        this.accountDetails = data;
        this.accountDetails.branch = 'SGB';
        this.accountDetails.ifscCode = 'SGB70000';
      },
      (error: any) => {
         this._toastService.error("Error fetching account details")
        console.log('Error fetching account details:', error);
      }
    );
  }
}
