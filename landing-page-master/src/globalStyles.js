import styled, {createGlobalStyle} from 'styled-components';

const GlobalStyles = createGlobalStyle`
*{
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    font-family: 'Source Sans Pro', sans-serif;
}
`;

export const Container = styled.div`
z-index: 1;
width: 100%;
max-width: 1300px;
margin-right: auto;
margin-left: auto;
padding-right: 50px;
padding-left: 50px;

@media screen and (max-width: 991px) {
    padding-right: 30px;
    padding-left: 30px;
}
`

export const Button = styled.button`
border-radius: 5px;
background: #4C566A;
color: #fff;
font-size: 14px;
outline: none;
border: none;
cursor: pointer;
padding: 10px 20px;
width: 50%;
height: 100%;

&:hover {
    transition: all 0.3s ease-in-out;
    background: #5E81AC;
    }

@media screen and (max-width: 991px) {
    width: 120px;
    height: 40px;   
}
`;

export const StyledTextArea = styled.input`
    background-color: #fff;
    height: 40px;
    border-radius: 5px;
    border: 1px solid #4C566A;
    margin: 10px 0 20px 0;
    padding: 20px;
    box-sizing: border-box;
    display: block;
    width: 70%;
    margin-top: -5%;

    @media screen and (max-width: 991px) {
    width: 100%;
    margin-top: 10%;
    
}
    
`;




export default GlobalStyles;