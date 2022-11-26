import { useEffect } from "react";
import { getHttpService } from "../data/httpService";
import history from "../components/history";

export interface LogoutPageProps {}

export function LogoutPage(props: LogoutPageProps) {
  const httpService = getHttpService();
  useEffect(() => {
    httpService.removeAuth();
    history.push("/f22-gneiss");
  });

  return (
    <div>
      <p>Logging you out...</p>
    </div>
  );
}
