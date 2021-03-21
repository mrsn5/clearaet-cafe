export interface OrderState {
    [key: number]: OrderUnit;
}

export interface OrderUnit {
    price: number;
    quantity: number;
}